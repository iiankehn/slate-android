package com.iiankehn.slate.io

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

class DocumentImportTest {
    @Test
    fun extensionDecidesEvenWhenProviderReportsGenericMimeType() {
        assertEquals(DocumentImport.Kind.Slx, DocumentImport.kindFor("Notes.SLX", "application/octet-stream"))
        assertEquals(DocumentImport.Kind.Slxf, DocumentImport.kindFor("Report.slxf", null))
        assertEquals(DocumentImport.Kind.Markdown, DocumentImport.kindFor("readme.markdown", "text/plain"))
        assertEquals(DocumentImport.Kind.Docx, DocumentImport.kindFor("letter.docx", "application/zip"))
    }

    @Test
    fun mimeTypeIsFallbackWhenProviderDropsExtension() {
        assertEquals(DocumentImport.Kind.Slxf, DocumentImport.kindFor("Report", SlxfCodec.MIME_TYPE))
        assertEquals(DocumentImport.Kind.Slx, DocumentImport.kindFor("Notes", SlxCodec.MIME_TYPE))
        assertEquals(DocumentImport.Kind.Markdown, DocumentImport.kindFor("1234", "text/markdown; charset=utf-8"))
        assertEquals(DocumentImport.Kind.Text, DocumentImport.kindFor("clipboard", "text/plain"))
    }

    @Test
    fun unsupportedFilesAreRejectedInsteadOfParsedAsText() {
        assertNull(DocumentImport.kindFor("photo.jpg", "image/jpeg"))
        assertNull(DocumentImport.kindFor("archive", "application/octet-stream"))
        assertNull(DocumentImport.kindFor("unknown", null))
    }

    @Test
    fun boundedReadAcceptsInputAtTheLimit() {
        val bytes = ByteArray(20_000) { it.toByte() }
        assertArrayEquals(bytes, DocumentImport.readBounded(ByteArrayInputStream(bytes), bytes.size))
    }

    @Test
    fun boundedReadStopsBeforeConsumingOversizedInput() {
        var consumed = 0L
        val endless = object : InputStream() {
            override fun read(): Int = 0
            override fun read(b: ByteArray, off: Int, len: Int): Int {
                consumed += len
                return len
            }
        }
        val error = assertThrows(IllegalArgumentException::class.java) {
            DocumentImport.readBounded(endless, 1024 * 1024)
        }
        assertEquals("This file is larger than the 1 MB import limit.", error.message)
        assertEquals(true, consumed <= 1024 * 1024 + DEFAULT_BUFFER_SIZE)
    }
}
