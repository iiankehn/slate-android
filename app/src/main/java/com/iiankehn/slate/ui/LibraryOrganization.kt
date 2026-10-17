package com.iiankehn.slate.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentTitlePolicy
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun LibraryOrganizationControls(
    documents: List<Document>,
    folder: String?,
    tag: String?,
    query: String,
    savedSearches: Set<String>,
    onFolder: (String?) -> Unit,
    onTag: (String?) -> Unit,
    onSaveSearch: () -> Unit,
    onUseSearch: (String) -> Unit,
    onRemoveSearch: (String) -> Unit,
) {
    val available = documents.filterNot { it.isDeleted }
    val folders = available.map { it.folder }.filter(String::isNotBlank).distinct().sorted()
    val tags = available.flatMap { it.tags }.distinct().sorted()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Folders", style = MaterialTheme.typography.labelLarge)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = folder == null, onClick = { onFolder(null) }, label = { Text("All folders") })
            folders.forEach { value ->
                FilterChip(selected = folder == value, onClick = { onFolder(if (folder == value) null else value) }, label = { Text(value) })
            }
        }
        Text("Tags", style = MaterialTheme.typography.labelLarge)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = tag == null, onClick = { onTag(null) }, label = { Text("All tags") })
            tags.forEach { value ->
                FilterChip(selected = tag == value, onClick = { onTag(if (tag == value) null else value) }, label = { Text(value) })
            }
        }
        Text("Saved searches", style = MaterialTheme.typography.labelLarge)
        TextButton(onClick = onSaveSearch, enabled = query.isNotBlank() && query.trim() !in savedSearches) { Text("Save current search") }
        if (savedSearches.isEmpty()) Text("Save search text to use it again.", style = MaterialTheme.typography.bodySmall)
        savedSearches.sorted().forEach { search ->
            Row(Modifier.fillMaxWidth()) {
                TextButton(onClick = { onUseSearch(search) }, modifier = Modifier.weight(1f)) {
                    Text(search, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                TextButton(onClick = { onRemoveSearch(search) }) { Text("Remove") }
            }
        }
    }
}

@Composable
internal fun OrganizationDialog(
    document: Document,
    folders: List<String>,
    onDismiss: () -> Unit,
    onSave: (String, Set<String>) -> Unit,
) {
    var folder by remember(document.id) { mutableStateOf(document.folder) }
    var tags by remember(document.id) { mutableStateOf(document.tags.sorted().joinToString(", ")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Folder & tags") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.heightIn(max = 360.dp)) {
                item { Text(DocumentTitlePolicy.displayTitle(document.title, document.body.text), maxLines = 2, overflow = TextOverflow.Ellipsis) }
                item {
                    OutlinedTextField(value = folder, onValueChange = { folder = it }, label = { Text("Folder") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                if (folders.isNotEmpty()) item {
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        folders.forEach { value -> FilterChip(selected = folder == value, onClick = { folder = value }, label = { Text(value) }) }
                    }
                }
                item {
                    OutlinedTextField(value = tags, onValueChange = { tags = it }, label = { Text("Tags, separated by commas") }, modifier = Modifier.fillMaxWidth())
                }
                item { Text("Leave a field empty to remove its folder or tags.", style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(folder.trim(), parseDocumentTags(tags)) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
internal fun VersionHistoryDialog(
    document: Document,
    versions: List<Document>?,
    onDismiss: () -> Unit,
    onRestore: (Document) -> Unit,
) {
    var pendingVersion by remember(document.id) { mutableStateOf<Document?>(null) }
    val formatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy · h:mm:ss a").withZone(ZoneId.systemDefault()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Version history") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.heightIn(max = 400.dp)) {
                item { Text(DocumentTitlePolicy.displayTitle(document.title, document.body.text), maxLines = 2, overflow = TextOverflow.Ellipsis) }
                when {
                    versions == null -> item { CircularProgressIndicator() }
                    versions.isEmpty() -> item { Text("No saved checkpoints yet. Slate creates them as you edit.") }
                    else -> items(versions) { version ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(formatter.format(Instant.ofEpochMilli(version.updatedAtEpochMillis)), style = MaterialTheme.typography.labelLarge)
                            Text(version.body.text.ifBlank { "Blank document" }, maxLines = 3, overflow = TextOverflow.Ellipsis)
                            TextButton(onClick = { pendingVersion = version }) { Text("Restore this version") }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )
    pendingVersion?.let { version ->
        AlertDialog(
            onDismissRequest = { pendingVersion = null },
            title = { Text("Restore this version?") },
            text = { Text("This replaces the document's current content. Slate keeps local checkpoints in version history.") },
            confirmButton = { TextButton(onClick = { pendingVersion = null; onRestore(version) }) { Text("Restore") } },
            dismissButton = { TextButton(onClick = { pendingVersion = null }) { Text("Cancel") } },
        )
    }
}
