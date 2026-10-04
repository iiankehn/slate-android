package com.iiankehn.slate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.iiankehn.slate.data.DocumentRepository
import com.iiankehn.slate.data.SlateDatabase
import com.iiankehn.slate.io.DocumentFormats
import com.iiankehn.slate.io.ImportedDocument
import com.iiankehn.slate.ui.SlateApp
import com.iiankehn.slate.ui.theme.SlateTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {
    private val slateViewModel: SlateViewModel by viewModels {
        SlateViewModel.Factory(
            DocumentRepository(SlateDatabase.getInstance(applicationContext).slateDao()),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        enableEdgeToEdge()
        setContent {
            SlateTheme {
                SlateApp(slateViewModel)
            }
        }
        handleDocumentIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDocumentIntent(intent)
    }

    private fun handleDocumentIntent(intent: Intent?) {
        val uri = intent?.data ?: return
        if (intent.action != Intent.ACTION_VIEW && intent.action != Intent.ACTION_EDIT) return
        lifecycleScope.launch {
            val imported = runCatching {
                withContext(Dispatchers.IO) {
                    val name = contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) cursor.getString(0) else "Imported document"
                    } ?: "Imported document"
                    val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: error("Unable to read document")
                    val title = name.substringBeforeLast('.').ifBlank { "Imported document" }
                    val imported = when (name.substringAfterLast('.', "").lowercase()) {
                        "slx" -> DocumentFormats.importSlx(bytes)
                        "md", "markdown" -> DocumentFormats.importMarkdown(bytes, title)
                        "docx" -> DocumentFormats.importDocx(bytes, title)
                        else -> DocumentFormats.importText(bytes, title)
                    }
                    materializeSlxAssets(imported)
                }
            }.getOrNull()
            if (imported != null) slateViewModel.importDocument(imported)
        }
    }

    private fun materializeSlxAssets(imported: ImportedDocument): ImportedDocument {
        if (imported.slxAssets.isEmpty()) return imported
        val directory = File(filesDir, "imported-slate-media").apply { mkdirs() }
        val uris = imported.slxAssets.associate { asset ->
            val extension = asset.extension.takeIf { it.matches(Regex("[A-Za-z0-9]{1,8}")) } ?: "bin"
            val file = File(directory, "${asset.id}-${asset.bytes.contentHashCode()}.$extension")
            file.outputStream().use { it.write(asset.bytes) }
            asset.id to Uri.fromFile(file).toString()
        }
        val body = imported.body.copy(ranges = imported.body.ranges.map { range ->
            val id = range.data?.takeIf { it.startsWith("asset:") }?.removePrefix("asset:")
            if (id != null && id in uris) range.copy(data = uris.getValue(id)) else range
        }).normalized()
        return imported.copy(body = body, slxAssets = emptyList())
    }
}
