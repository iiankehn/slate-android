package com.iiankehn.slate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ForgeImportDialog(
    busy: Boolean,
    progress: String,
    results: List<String>,
    onChoose: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bring your Forge documents") },
        text = {
            LazyColumn(Modifier.heightIn(max = 400.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("1. Open the former Slate Forge app and export your documents as .slxf for full formatting, or .slx for notes.") }
                item { Text("2. Save the exports in a folder you can find in Android’s file picker.") }
                item { Text("3. Choose the exported files below. You can select several at once.") }
                item { Text("Slate imports new copies into this library. Your source files and existing documents stay intact. Selecting a file again creates another copy.", style = MaterialTheme.typography.bodySmall) }
                item { Text("Keep the old app and exports until you have checked the imported text, layout and images.", style = MaterialTheme.typography.bodySmall) }
                if (busy) item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        LinearProgressIndicator()
                        Text(progress)
                    }
                }
                items(results) { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = { TextButton(onClick = onChoose, enabled = !busy) { Text(if (results.isEmpty()) "Choose exports" else "Choose more exports") } },
        dismissButton = { TextButton(onClick = onDismiss, enabled = !busy) { Text("Close") } },
    )
}
