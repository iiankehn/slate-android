package com.iiankehn.slate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iiankehn.slate.SlateViewModel
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentTitlePolicy
import com.iiankehn.slate.model.RichTextDocument
import com.iiankehn.slate.model.RichTextStyle
import com.iiankehn.slate.ui.theme.CoreBlue
import com.iiankehn.slate.ui.theme.Midnight
import com.iiankehn.slate.ui.theme.SlateSurfaceRaised
import com.iiankehn.slate.ui.theme.SlateTextMuted

private enum class CompactDestination { Library, Editor }

@Composable
fun SlateApp(viewModel: SlateViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val documents = uiState.documents
    var selectedId by remember { mutableStateOf<String?>(null) }
    var destination by remember { mutableStateOf(CompactDestination.Library) }

    LaunchedEffect(documents) {
        if (documents.none { it.id == selectedId }) {
            selectedId = documents.firstOrNull { !it.isArchived }?.id ?: documents.firstOrNull()?.id
        }
    }

    if (uiState.loading || documents.isEmpty()) {
        LoadingSlate()
        return
    }

    val selected = documents.firstOrNull { it.id == selectedId }
        ?: documents.firstOrNull { !it.isArchived }
        ?: documents.first()

    fun newDocument() {
        selectedId = viewModel.createDocument().id
    }

    fun selectAfterRemoval(documentId: String) {
        val replacement = documents.firstOrNull { it.id != documentId && !it.isArchived }
        selectedId = replacement?.id ?: viewModel.createDocument().id
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(CoreBlue.copy(alpha = 0.20f), Midnight),
                    radius = 1100f,
                ),
            ),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            val expanded = maxWidth >= 840.dp
            if (expanded) {
                Row(Modifier.fillMaxSize()) {
                    DocumentLibrary(
                        documents = documents,
                        selectedId = selected.id,
                        onDocumentSelected = { selectedId = it.id },
                        onNewDocument = { newDocument() },
                        modifier = Modifier.width(360.dp).fillMaxHeight(),
                    )
                    Editor(
                        document = selected,
                        saving = selected.id in uiState.savingDocumentIds,
                        onDocumentChange = viewModel::updateDocument,
                        onDuplicate = { selectedId = viewModel.duplicateDocument(it).id },
                        onTogglePin = { viewModel.togglePin(it) },
                        onArchive = {
                            val changed = viewModel.toggleArchive(it)
                            if (changed.isArchived) selectAfterRemoval(it.id) else selectedId = changed.id
                        },
                        onDelete = {
                            viewModel.deleteDocument(it)
                            selectAfterRemoval(it.id)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            } else if (destination == CompactDestination.Library) {
                DocumentLibrary(
                    documents = documents,
                    selectedId = selected.id,
                    onDocumentSelected = {
                        selectedId = it.id
                        destination = CompactDestination.Editor
                    },
                    onNewDocument = {
                        newDocument()
                        destination = CompactDestination.Editor
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Editor(
                    document = selected,
                    saving = selected.id in uiState.savingDocumentIds,
                    onDocumentChange = viewModel::updateDocument,
                    onDuplicate = { selectedId = viewModel.duplicateDocument(it).id },
                    onTogglePin = { viewModel.togglePin(it) },
                    onArchive = {
                        val changed = viewModel.toggleArchive(it)
                        if (changed.isArchived) selectAfterRemoval(it.id) else selectedId = changed.id
                        destination = CompactDestination.Library
                    },
                    onDelete = {
                        viewModel.deleteDocument(it)
                        selectAfterRemoval(it.id)
                        destination = CompactDestination.Library
                    },
                    onBack = { destination = CompactDestination.Library },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun LoadingSlate() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Midnight),
        contentAlignment = Alignment.Center,
    ) {
        Text("Loading Slate…", color = SlateTextMuted)
    }
}

@Composable
private fun DocumentLibrary(
    documents: List<Document>,
    selectedId: String,
    onDocumentSelected: (Document) -> Unit,
    onNewDocument: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activeDocuments = documents.filterNot(Document::isArchived)
    val archivedDocuments = documents.filter(Document::isArchived)

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
        modifier = modifier,
    ) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
            ) {
                Column(Modifier.weight(1f)) {
                    Text("SLATE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoreBlue, letterSpacing = 2.sp)
                    Text("Documents", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onNewDocument,
                    shape = CircleShape,
                    contentPadding = ButtonDefaults.ContentPadding,
                ) { Text("New") }
            }

            Text(
                "On this device",
                color = SlateTextMuted,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp),
            )

            if (activeDocuments.isEmpty() && archivedDocuments.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No documents yet", color = SlateTextMuted)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                ) {
                    items(activeDocuments, key = { it.id }) { document ->
                        DocumentRow(document, selectedId == document.id) { onDocumentSelected(document) }
                    }
                    if (archivedDocuments.isNotEmpty()) {
                        item(key = "archived-heading") {
                            Text(
                                "Archived",
                                color = SlateTextMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                            )
                        }
                    }
                    items(archivedDocuments, key = { "archived-${it.id}" }) { document ->
                        DocumentRow(document, selectedId == document.id) { onDocumentSelected(document) }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentRow(
    document: Document,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) CoreBlue.copy(alpha = 0.23f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 13.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (document.isPinned && !document.isArchived) {
                Box(Modifier.size(7.dp).background(CoreBlue, CircleShape))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                DocumentTitlePolicy.displayTitle(document.title, document.body.text),
                color = if (document.isArchived) SlateTextMuted else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
        Text(document.updatedLabel, color = SlateTextMuted, fontSize = 12.sp)
    }
}

private data class EditorSnapshot(
    val title: String,
    val body: RichTextDocument,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Editor(
    document: Document,
    saving: Boolean,
    onDocumentChange: (Document) -> Unit,
    onDuplicate: (Document) -> Unit,
    onTogglePin: (Document) -> Unit,
    onArchive: (Document) -> Unit,
    onDelete: (Document) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    val undoStack = remember(document.id) { mutableStateListOf<EditorSnapshot>() }
    val redoStack = remember(document.id) { mutableStateListOf<EditorSnapshot>() }
    val titleFocusRequester = remember(document.id) { FocusRequester() }
    var renameRequest by remember(document.id) { mutableIntStateOf(0) }
    var menuExpanded by remember(document.id) { mutableStateOf(false) }
    var confirmDelete by remember(document.id) { mutableStateOf(false) }
    var bodyValue by remember(document.id) {
        mutableStateOf(
            TextFieldValue(
                annotatedString = annotatedBody(document.body),
                selection = TextRange(document.body.text.length),
            ),
        )
    }

    LaunchedEffect(document.body) {
        val annotated = annotatedBody(document.body)
        if (bodyValue.text != document.body.text || bodyValue.annotatedString != annotated) {
            bodyValue = TextFieldValue(
                annotatedString = annotated,
                selection = bodyValue.selection.coerceIn(0, document.body.text.length),
            )
        }
    }

    LaunchedEffect(renameRequest) {
        if (renameRequest > 0) titleFocusRequester.requestFocus()
    }

    fun commit(changed: Document) {
        val before = EditorSnapshot(document.title, document.body)
        val after = EditorSnapshot(changed.title, changed.body)
        if (before == after) return
        if (undoStack.size == 100) undoStack.removeAt(0)
        undoStack += before
        redoStack.clear()
        onDocumentChange(changed.copy(updatedLabel = "Just now"))
    }

    fun applyStyle(style: RichTextStyle, blockStyle: Boolean = false) {
        val target = if (blockStyle) {
            paragraphRange(document.body.text, bodyValue.selection)
        } else {
            selectionOrWordRange(document.body.text, bodyValue.selection)
        }
        if (target.start == target.end) return
        val body = document.body.toggle(style, target.start, target.end)
        bodyValue = TextFieldValue(annotatedBody(body), bodyValue.selection)
        commit(document.copy(body = body))
    }

    fun applyPrefix(prefix: String) {
        val (body, selection) = toggleLinePrefix(document.body, bodyValue.selection, prefix)
        if (body == document.body) return
        bodyValue = TextFieldValue(annotatedBody(body), selection)
        commit(document.copy(body = body))
    }

    Column(modifier.background(Color.Transparent).navigationBarsPadding()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(backIcon, contentDescription = "Back")
                }
            }
            Text(
                if (saving) "Saving…" else "Saved on device",
                color = SlateTextMuted,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
            )
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Text("•••", color = SlateTextMuted, fontWeight = FontWeight.Bold)
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        onClick = {
                            menuExpanded = false
                            renameRequest += 1
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Duplicate") },
                        onClick = {
                            menuExpanded = false
                            onDuplicate(document)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(if (document.isPinned) "Unpin" else "Pin") },
                        onClick = {
                            menuExpanded = false
                            onTogglePin(document)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(if (document.isArchived) "Restore" else "Archive") },
                        onClick = {
                            menuExpanded = false
                            onArchive(document)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            menuExpanded = false
                            confirmDelete = true
                        },
                    )
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 20.dp)) {
                BasicTextField(
                    value = document.title,
                    onValueChange = { commit(document.copy(title = it)) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 32.sp, fontWeight = FontWeight.Bold),
                    cursorBrush = SolidColor(CoreBlue),
                    decorationBox = { field ->
                        Box {
                            if (document.title.isBlank()) Text("Untitled", color = SlateTextMuted, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().focusRequester(titleFocusRequester),
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 18.dp),
                ) {
                    FormattingButton(
                        label = "B",
                        active = document.body.hasStyle(RichTextStyle.Bold, bodyValue.selection.start, bodyValue.selection.end),
                        onClick = { applyStyle(RichTextStyle.Bold) },
                    )
                    FormattingButton(
                        label = "I",
                        active = document.body.hasStyle(RichTextStyle.Italic, bodyValue.selection.start, bodyValue.selection.end),
                        onClick = { applyStyle(RichTextStyle.Italic) },
                    )
                    FormattingButton(
                        label = "U",
                        active = document.body.hasStyle(RichTextStyle.Underline, bodyValue.selection.start, bodyValue.selection.end),
                        onClick = { applyStyle(RichTextStyle.Underline) },
                    )
                    FormattingButton(
                        label = "H1",
                        active = document.body.hasStyle(RichTextStyle.HeadingOne, bodyValue.selection.start, bodyValue.selection.end),
                        onClick = { applyStyle(RichTextStyle.HeadingOne, blockStyle = true) },
                    )
                    FormattingButton(label = "List", onClick = { applyPrefix("• ") })
                    FormattingButton(label = "Check", onClick = { applyPrefix("☐ ") })
                    FormattingButton(
                        label = "↶",
                        enabled = undoStack.isNotEmpty(),
                        onClick = {
                            val previous = undoStack.removeAt(undoStack.lastIndex)
                            redoStack += EditorSnapshot(document.title, document.body)
                            onDocumentChange(document.copy(title = previous.title, body = previous.body, updatedLabel = "Just now"))
                        },
                    )
                    FormattingButton(
                        label = "↷",
                        enabled = redoStack.isNotEmpty(),
                        onClick = {
                            val next = redoStack.removeAt(redoStack.lastIndex)
                            undoStack += EditorSnapshot(document.title, document.body)
                            onDocumentChange(document.copy(title = next.title, body = next.body, updatedLabel = "Just now"))
                        },
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                BasicTextField(
                    value = bodyValue,
                    onValueChange = { changed ->
                        val body = document.body.updateText(changed.text)
                        bodyValue = TextFieldValue(annotatedBody(body), changed.selection)
                        commit(document.copy(body = body))
                    },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, lineHeight = 29.sp),
                    cursorBrush = SolidColor(CoreBlue),
                    decorationBox = { field ->
                        Box(Modifier.padding(top = 18.dp)) {
                            if (document.body.text.isBlank()) Text("Start writing…", color = SlateTextMuted, fontSize = 18.sp)
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete document?") },
            text = { Text("This removes the document from this prototype. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        onDelete(document)
                    },
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun FormattingButton(
    label: String,
    active: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Surface(
        color = if (active) CoreBlue.copy(alpha = 0.45f) else SlateSurfaceRaised,
        contentColor = if (enabled) MaterialTheme.colorScheme.onSurface else SlateTextMuted.copy(alpha = 0.45f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

private fun annotatedBody(body: RichTextDocument): AnnotatedString = AnnotatedString.Builder(body.text).apply {
    body.normalized().ranges.forEach { range ->
        val style = when (range.style) {
            RichTextStyle.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
            RichTextStyle.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
            RichTextStyle.Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
            RichTextStyle.HeadingOne -> SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        addStyle(style, range.start, range.end)
    }
}.toAnnotatedString()

private fun TextRange.coerceIn(minimum: Int, maximum: Int): TextRange = TextRange(
    start.coerceIn(minimum, maximum),
    end.coerceIn(minimum, maximum),
)

private fun selectionOrWordRange(text: String, selection: TextRange): TextRange {
    if (!selection.collapsed) return selection.coerceIn(0, text.length)
    if (text.isEmpty()) return TextRange.Zero

    val cursor = selection.start.coerceIn(0, text.length)
    var start = cursor
    var end = cursor
    while (start > 0 && !text[start - 1].isWhitespace()) start -= 1
    while (end < text.length && !text[end].isWhitespace()) end += 1
    return TextRange(start, end)
}

private fun paragraphRange(text: String, selection: TextRange): TextRange {
    if (text.isEmpty()) return TextRange.Zero
    val safe = selection.coerceIn(0, text.length)
    val searchStart = (safe.min - 1).coerceAtLeast(0)
    val start = text.lastIndexOf('\n', searchStart).let { if (it < 0) 0 else it + 1 }
    val end = text.indexOf('\n', safe.max).let { if (it < 0) text.length else it }
    return TextRange(start, end)
}

private fun toggleLinePrefix(
    body: RichTextDocument,
    selection: TextRange,
    prefix: String,
): Pair<RichTextDocument, TextRange> {
    val range = paragraphRange(body.text, selection)
    if (range.start == range.end && body.text.isEmpty()) return body to selection

    val original = body.text.substring(range.start, range.end)
    val lines = original.split('\n')
    val removePrefix = lines.all { it.startsWith(prefix) }
    val replacement = lines.joinToString("\n") { line ->
        if (removePrefix) line.removePrefix(prefix) else prefix + line
    }
    val updatedText = body.text.replaceRange(range.start, range.end, replacement)
    val updatedBody = body.updateText(updatedText)
    return updatedBody to TextRange(range.start, range.start + replacement.length)
}

private val backIcon: ImageVector = ImageVector.Builder(
    name = "Back",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.White)) {
        moveTo(20f, 11f)
        horizontalLineTo(7.83f)
        lineTo(13.42f, 5.41f)
        lineTo(12f, 4f)
        lineTo(4f, 12f)
        lineTo(12f, 20f)
        lineTo(13.42f, 18.59f)
        lineTo(7.83f, 13f)
        horizontalLineTo(20f)
        close()
    }
}.build()
