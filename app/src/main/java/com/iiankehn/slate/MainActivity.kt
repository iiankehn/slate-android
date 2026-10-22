package com.iiankehn.slate

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.iiankehn.slate.data.DocumentRepository
import com.iiankehn.slate.data.SlateDatabase
import com.iiankehn.slate.io.DocumentImport
import com.iiankehn.slate.ui.SlateApp
import com.iiankehn.slate.ui.theme.SlateTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        // A recreated activity still holds the original intent; importing it again would duplicate the document.
        if (savedInstanceState == null) handleDocumentIntent(intent)
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
            runCatching {
                withContext(Dispatchers.IO) { DocumentImport.fromUri(applicationContext, uri) }
            }.onSuccess { imported ->
                slateViewModel.importDocument(imported)
                if (imported.warnings.isNotEmpty()) Toast.makeText(this@MainActivity, imported.warnings.joinToString(" "), Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this@MainActivity, it.message ?: "Unable to open this document.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
