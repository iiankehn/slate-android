package com.iiankehn.slate.io

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.iiankehn.slate.model.ImageBlock
import java.io.File
import java.io.InputStream

/** Shared import path for the in-app picker and external VIEW/EDIT intents. */
object DocumentImport {
    enum class Kind(val maxBytes: Int) {
        Slx(32 * 1024 * 1024),
        Slxf(64 * 1024 * 1024),
        Markdown(DocumentFormats.MAX_DOCUMENT_BYTES),
        Docx(DocumentFormats.MAX_DOCUMENT_BYTES),
        Text(DocumentFormats.MAX_DOCUMENT_BYTES),
    }

    const val UNSUPPORTED_MESSAGE = "Slate can import .slx, .slxf, .txt, .md, .markdown, and .docx files."
    private const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    /**
     * The file name decides when it has a known extension. Some providers drop or rename extensions,
     * so the reported MIME type is the fallback rather than the only signal.
     */
    fun kindFor(name: String, mimeType: String?): Kind? {
        val byExtension = when (name.substringAfterLast('.', "").lowercase()) {
            SlxCodec.FILE_EXTENSION -> Kind.Slx
            SlxfCodec.FILE_EXTENSION -> Kind.Slxf
            "md", "markdown" -> Kind.Markdown
            "docx" -> Kind.Docx
            "txt" -> Kind.Text
            else -> null
        }
        if (byExtension != null) return byExtension
        val mime = mimeType?.substringBefore(';')?.trim()?.lowercase() ?: return null
        return when {
            mime == SlxCodec.MIME_TYPE -> Kind.Slx
            mime == SlxfCodec.MIME_TYPE -> Kind.Slxf
            mime == "text/markdown" || mime == "text/x-markdown" -> Kind.Markdown
            mime == DOCX_MIME_TYPE -> Kind.Docx
            mime.startsWith("text/") -> Kind.Text
            else -> null
        }
    }

    /** Reads at most [limit] bytes so oversized input fails before it is fully allocated. */
    fun readBounded(input: InputStream, limit: Int, tooLargeMessage: String = "This file is larger than the ${limit / (1024 * 1024)} MB import limit."): ByteArray {
        val output = java.io.ByteArrayOutputStream()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            require(output.size().toLong() + count <= limit) { tooLargeMessage }
            output.write(buffer, 0, count)
        }
        return output.toByteArray()
    }

    fun parse(bytes: ByteArray, kind: Kind, title: String): ImportedDocument = when (kind) {
        Kind.Slx -> DocumentFormats.importSlx(bytes)
        Kind.Slxf -> DocumentFormats.importSlxf(bytes)
        Kind.Markdown -> DocumentFormats.importMarkdown(bytes, title)
        Kind.Docx -> DocumentFormats.importDocx(bytes, title)
        Kind.Text -> DocumentFormats.importText(bytes, title)
    }

    /** Blocking; call off the main thread. Throws with a user-readable message on failure. */
    fun fromUri(context: Context, uri: Uri): ImportedDocument {
        val resolver = context.contentResolver
        val name = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } ?: uri.lastPathSegment ?: "Imported document"
        val kind = kindFor(name, resolver.getType(uri)) ?: error(UNSUPPORTED_MESSAGE)
        val bytes = resolver.openInputStream(uri)?.use { readBounded(it, kind.maxBytes) }
            ?: error("Unable to read the selected document.")
        val title = name.substringBeforeLast('.').ifBlank { "Imported document" }
        return materializeAssets(context, parse(bytes, kind, title))
    }

    /** Writes embedded images and package assets to app-private storage and points the document at them. */
    fun materializeAssets(context: Context, imported: ImportedDocument): ImportedDocument {
        if (imported.embeddedImages.isEmpty() && imported.slxAssets.isEmpty()) return imported
        val directory = File(context.filesDir, "imported-slate-media").apply { mkdirs() }
        fun write(id: String, extension: String, bytes: ByteArray): String {
            val safeExtension = extension.takeIf { it.matches(Regex("[A-Za-z0-9]{1,8}")) } ?: "bin"
            val file = File(directory, "$id-${bytes.contentHashCode()}.$safeExtension")
            file.outputStream().use { it.write(bytes) }
            return Uri.fromFile(file).toString()
        }
        val forgeUris = imported.embeddedImages.associate { it.blockId to write(it.blockId, it.extension, it.bytes) }
        val sharedUris = imported.slxAssets.associate { it.id to write(it.id, it.extension, it.bytes) }
        val body = imported.body.copy(ranges = imported.body.ranges.map { range ->
            val id = range.data?.takeIf { it.startsWith("asset:") }?.removePrefix("asset:")
            if (id != null && id in sharedUris) range.copy(data = sharedUris.getValue(id)) else range
        }).normalized()
        val forge = imported.wordProcessingDocument?.let { document ->
            document.copy(sections = document.sections.map { section ->
                section.copy(blocks = section.blocks.map { block ->
                    if (block is ImageBlock && block.id in forgeUris) block.copy(sourceUri = forgeUris.getValue(block.id)) else block
                })
            })
        }
        return imported.copy(body = body, wordProcessingDocument = forge, embeddedImages = emptyList(), slxAssets = emptyList())
    }
}
