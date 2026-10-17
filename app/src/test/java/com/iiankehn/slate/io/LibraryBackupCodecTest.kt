package com.iiankehn.slate.io

import com.iiankehn.slate.model.DocumentExperience
import com.iiankehn.slate.model.ParagraphBlock
import com.iiankehn.slate.model.DocumentSection
import com.iiankehn.slate.model.WordProcessingDocument
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryBackupCodecTest {
    @Test
    fun encryptedRoundTripPreservesMetadataAndPackage() {
        val packageBytes = packageBytes("doc-1", "Private draft", "Hello, Slate")
        val source = LibraryBackup(
            createdAtEpochMillis = 1_800_000_000_000,
            documents = listOf(
                LibraryBackupDocument(
                    id = "doc-1",
                    title = "Private draft",
                    updatedAtEpochMillis = 1_799_999_000_000,
                    isPinned = true,
                    isArchived = false,
                    isFavorite = true,
                    isDeleted = false,
                    folder = "Writing",
                    tags = setOf("draft", "personal"),
                    experience = DocumentExperience.Forge,
                    slxfPackage = packageBytes,
                ),
            ),
        )

        val encoded = LibraryBackupCodec.encrypt(source, "correct horse".toCharArray())
        val decoded = LibraryBackupCodec.decrypt(encoded, "correct horse".toCharArray())

        assertEquals(source.createdAtEpochMillis, decoded.createdAtEpochMillis)
        assertEquals(1, decoded.documents.size)
        val document = decoded.documents.single()
        assertEquals("doc-1", document.id)
        assertEquals("Private draft", document.title)
        assertEquals(setOf("draft", "personal"), document.tags)
        assertEquals(DocumentExperience.Forge, document.experience)
        assertEquals("Hello, Slate", SlxfCodec.decode(document.slxfPackage).document.sections.single().blocks.filterIsInstance<ParagraphBlock>().single().runs.single().text)
    }

    @Test
    fun wrongPassphraseIsRejected() {
        val encoded = LibraryBackupCodec.encrypt(LibraryBackup(1, emptyList()), "right-pass".toCharArray())
        val error = assertThrows(IllegalArgumentException::class.java) {
            LibraryBackupCodec.decrypt(encoded, "wrong-pass".toCharArray())
        }
        assertTrue(error.message.orEmpty().contains("incorrect"))
    }

    @Test
    fun modifiedCiphertextIsRejected() {
        val encoded = LibraryBackupCodec.encrypt(LibraryBackup(1, emptyList()), "right-pass".toCharArray())
        encoded[encoded.lastIndex - 32] = (encoded[encoded.lastIndex - 32].toInt() xor 1).toByte()
        val error = assertThrows(IllegalArgumentException::class.java) {
            LibraryBackupCodec.decrypt(encoded, "right-pass".toCharArray())
        }
        assertTrue(error.message.orEmpty().contains("integrity"))
    }

    @Test
    fun shortPassphraseIsRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            LibraryBackupCodec.encrypt(LibraryBackup(1, emptyList()), "short".toCharArray())
        }
    }

    private fun packageBytes(id: String, title: String, text: String): ByteArray = SlxfCodec.encode(
        SlxfDocument(
            document = WordProcessingDocument(
                id = id,
                title = title,
                sections = listOf(
                    DocumentSection(
                        blocks = listOf(
                            ParagraphBlock(runs = listOf(com.iiankehn.slate.model.TextRun(text))),
                        ),
                    ),
                ),
            ),
            sourceRevision = 1,
        ),
    )
}
