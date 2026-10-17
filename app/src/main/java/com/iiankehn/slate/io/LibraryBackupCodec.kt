package com.iiankehn.slate.io

import com.iiankehn.slate.model.DocumentExperience
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class LibraryBackupDocument(
    val id: String,
    val title: String,
    val updatedAtEpochMillis: Long,
    val isPinned: Boolean,
    val isArchived: Boolean,
    val isFavorite: Boolean,
    val isDeleted: Boolean,
    val folder: String,
    val tags: Set<String>,
    val experience: DocumentExperience,
    val slxfPackage: ByteArray,
)

data class LibraryBackup(
    val createdAtEpochMillis: Long,
    val documents: List<LibraryBackupDocument>,
)

/** Password-encrypted, authenticated whole-library container used by manual backup and restore. */
object LibraryBackupCodec {
    const val MIME_TYPE = "application/vnd.core.slate.backup"
    const val FILE_EXTENSION = "slatebackup"
    const val FORMAT_VERSION = 1

    private const val MAGIC = 0x534C424B // SLBK
    private const val PAYLOAD_MAGIC = 0x534C4250 // SLBP
    private const val KDF_ITERATIONS = 210_000
    private const val KEY_BITS = 256
    private const val SALT_BYTES = 16
    private const val NONCE_BYTES = 12
    private const val DIGEST_BYTES = 32
    private const val MAX_BACKUP_BYTES = 512 * 1024 * 1024
    private const val MAX_DOCUMENTS = 50_000
    private const val MAX_STRING_BYTES = 16 * 1024 * 1024
    private const val MAX_DOCUMENT_BYTES = 64 * 1024 * 1024

    fun encrypt(backup: LibraryBackup, passphrase: CharArray, random: SecureRandom = SecureRandom()): ByteArray {
        require(passphrase.size >= 8) { "Use a backup passphrase with at least 8 characters." }
        require(backup.documents.size <= MAX_DOCUMENTS) { "The library contains too many documents to back up." }
        val plaintext = encodePayload(backup)
        require(plaintext.size <= MAX_BACKUP_BYTES) { "The backup exceeds the 512 MB limit." }
        val salt = ByteArray(SALT_BYTES).also(random::nextBytes)
        val nonce = ByteArray(NONCE_BYTES).also(random::nextBytes)
        val key = deriveKey(passphrase, salt, KDF_ITERATIONS)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, nonce))
        cipher.updateAAD(headerBytes(KDF_ITERATIONS, salt, nonce))
        val ciphertext = cipher.doFinal(plaintext)
        require(ciphertext.size <= MAX_BACKUP_BYTES) { "The encrypted backup exceeds the 512 MB limit." }
        return ByteArrayOutputStream().also { output ->
            DataOutputStream(output).use { data ->
                data.writeInt(MAGIC)
                data.writeInt(FORMAT_VERSION)
                data.writeInt(KDF_ITERATIONS)
                data.writeInt(salt.size); data.write(salt)
                data.writeInt(nonce.size); data.write(nonce)
                data.writeInt(ciphertext.size); data.write(ciphertext)
                data.write(ciphertext.sha256())
            }
        }.toByteArray()
    }

    fun decrypt(bytes: ByteArray, passphrase: CharArray): LibraryBackup {
        require(passphrase.isNotEmpty()) { "Enter the backup passphrase." }
        require(bytes.size <= MAX_BACKUP_BYTES + 1_024) { "The backup exceeds the 512 MB limit." }
        return DataInputStream(ByteArrayInputStream(bytes)).use { data ->
            require(data.readInt() == MAGIC) { "This is not a Slate library backup." }
            val version = data.readInt()
            require(version in 1..FORMAT_VERSION) { "Unsupported Slate backup version: $version." }
            val iterations = data.readInt()
            require(iterations in 100_000..1_000_000) { "The backup has invalid encryption settings." }
            val salt = data.readSizedBytes(SALT_BYTES, SALT_BYTES, "salt")
            val nonce = data.readSizedBytes(NONCE_BYTES, NONCE_BYTES, "nonce")
            val ciphertext = data.readSizedBytes(1, MAX_BACKUP_BYTES, "encrypted payload")
            val expectedDigest = ByteArray(DIGEST_BYTES).also(data::readFully)
            require(data.read() == -1) { "The backup contains unexpected trailing data." }
            require(ciphertext.sha256().contentEquals(expectedDigest)) { "The backup failed its integrity check." }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, deriveKey(passphrase, salt, iterations), GCMParameterSpec(128, nonce))
            cipher.updateAAD(headerBytes(iterations, salt, nonce))
            val plaintext = runCatching { cipher.doFinal(ciphertext) }
                .getOrElse { throw IllegalArgumentException("The passphrase is incorrect or the backup was modified.") }
            decodePayload(plaintext)
        }
    }

    private fun encodePayload(backup: LibraryBackup): ByteArray = ByteArrayOutputStream().also { output ->
        DataOutputStream(output).use { data ->
            data.writeInt(PAYLOAD_MAGIC)
            data.writeInt(FORMAT_VERSION)
            data.writeLong(backup.createdAtEpochMillis)
            data.writeInt(backup.documents.size)
            backup.documents.forEach { document ->
                require(document.slxfPackage.size <= MAX_DOCUMENT_BYTES) { "${document.title.ifBlank { "A document" }} exceeds the 64 MB backup limit." }
                data.writeString(document.id)
                data.writeString(document.title)
                data.writeLong(document.updatedAtEpochMillis)
                data.writeBoolean(document.isPinned)
                data.writeBoolean(document.isArchived)
                data.writeBoolean(document.isFavorite)
                data.writeBoolean(document.isDeleted)
                data.writeString(document.folder)
                data.writeInt(document.tags.size)
                document.tags.sorted().forEach { tag -> data.writeString(tag) }
                data.writeString(document.experience.name)
                data.writeInt(document.slxfPackage.size)
                data.write(document.slxfPackage)
            }
        }
    }.toByteArray()

    private fun decodePayload(bytes: ByteArray): LibraryBackup = DataInputStream(ByteArrayInputStream(bytes)).use { data ->
        require(data.readInt() == PAYLOAD_MAGIC) { "The decrypted backup payload is invalid." }
        val version = data.readInt()
        require(version in 1..FORMAT_VERSION) { "Unsupported backup payload version: $version." }
        val createdAt = data.readLong()
        val count = data.readInt()
        require(count in 0..MAX_DOCUMENTS) { "The backup has an invalid document count." }
        val documents = List(count) {
            val id = data.readString()
            val title = data.readString()
            val updatedAt = data.readLong()
            val pinned = data.readBoolean()
            val archived = data.readBoolean()
            val favorite = data.readBoolean()
            val deleted = data.readBoolean()
            val folder = data.readString()
            val tagCount = data.readInt()
            require(tagCount in 0..10_000) { "A backup document has too many tags." }
            val tags = List(tagCount) { data.readString() }.toSet()
            val experienceName = data.readString()
            val experience = DocumentExperience.entries.firstOrNull { it.name == experienceName }
                ?: DocumentExperience.Adaptive
            val packageBytes = data.readSizedBytes(1, MAX_DOCUMENT_BYTES, "document package")
            // Decode now so corrupt nested packages fail before the restore preview is shown.
            SlxfCodec.decode(packageBytes)
            LibraryBackupDocument(id, title, updatedAt, pinned, archived, favorite, deleted, folder, tags, experience, packageBytes)
        }
        require(data.read() == -1) { "The backup payload contains unexpected trailing data." }
        LibraryBackup(createdAt, documents)
    }

    private fun deriveKey(passphrase: CharArray, salt: ByteArray, iterations: Int): SecretKeySpec {
        val spec = PBEKeySpec(passphrase, salt, iterations, KEY_BITS)
        return try {
            SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded, "AES")
        } finally {
            spec.clearPassword()
        }
    }

    private fun headerBytes(iterations: Int, salt: ByteArray, nonce: ByteArray) = ByteArrayOutputStream().also { output ->
        DataOutputStream(output).use { data ->
            data.writeInt(MAGIC); data.writeInt(FORMAT_VERSION); data.writeInt(iterations)
            data.writeInt(salt.size); data.write(salt); data.writeInt(nonce.size); data.write(nonce)
        }
    }.toByteArray()

    private fun DataOutputStream.writeString(value: String) {
        val encoded = value.toByteArray(Charsets.UTF_8)
        require(encoded.size <= MAX_STRING_BYTES) { "Backup text value is too large." }
        writeInt(encoded.size); write(encoded)
    }

    private fun DataInputStream.readString(): String {
        val size = readInt()
        require(size in 0..MAX_STRING_BYTES) { "Backup text value is invalid." }
        return ByteArray(size).also(::readFully).toString(Charsets.UTF_8)
    }

    private fun DataInputStream.readSizedBytes(min: Int, max: Int, label: String): ByteArray {
        val size = readInt()
        require(size in min..max) { "The backup $label size is invalid." }
        return ByteArray(size).also(::readFully)
    }

    private fun ByteArray.sha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)
}
