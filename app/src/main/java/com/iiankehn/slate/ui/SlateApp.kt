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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentTitlePolicy
import com.iiankehn.slate.ui.theme.CoreBlue
import com.iiankehn.slate.ui.theme.Midnight

private val starterDocuments = listOf(
    Document(
        "welcome",
        "Welcome to Slate",
        "A calm place for notes, drafts, and complete documents.\n\nEverything starts on your device.",
        "Just now",
        true,
    ),
    Document(
        "ideas",
        "Project ideas",
        "Build the smallest useful version first.\nKeep the editor fast.\nRespect the writer's privacy.",
        "12 min ago",
    ),
    Document(
        "meeting",
        "Meeting notes",
        "Agenda\n\n• Current work\n• Decisions\n• Next steps",
        "Yesterday",
    ),
)

private enum class CompactDestination { Library, Editor }

@Composable
fun SlateApp() {
    var selected by remember { mutableStateOf(starterDocuments.first()) }
    var destination by remember { mutableStateOf(CompactDestination.Library) }
    var documents by remember { mutableStateOf(starterDocuments) }

    fun createDocument(): Document {
        val document = Document("new-${documents.size}", "", "", "Just now")
        documents = listOf(document) + documents
        selected = document
        return document
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.surfaceContainer, Midnight),
                ),
            )
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val expanded = maxWidth >= 720.dp

            if (expanded) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DocumentLibrary(
                        documents = documents,
                        selectedId = selected.id,
                        onDocumentSelected = { selected = it },
                        onNewDocument = { createDocument() },
                        modifier = Modifier.width(340.dp).fillMaxHeight(),
                    )
                    Editor(
                        document = selected,
                        onDocumentChange = { changed ->
                            selected = changed
                            documents = documents.map { if (it.id == changed.id) changed else it }
                        },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            } else if (destination == CompactDestination.Library) {
                DocumentLibrary(
                    documents = documents,
                    selectedId = selected.id,
                    onDocumentSelected = {
                        selected = it
                        destination = CompactDestination.Editor
                    },
                    onNewDocument = {
                        createDocument()
                        destination = CompactDestination.Editor
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Editor(
                    document = selected,
                    onDocumentChange = { changed ->
                        selected = changed
                        documents = documents.map { if (it.id == changed.id) changed else it }
                    },
                    onBack = { destination = CompactDestination.Library },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
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
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 2.dp,
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                LibraryHeader()
                Text(
                    text = "ON THIS DEVICE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 10.dp),
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                ) {
                    items(documents, key = { it.id }) { document ->
                        DocumentRow(
                            document = document,
                            selected = selectedId == document.id,
                            onClick = { onDocumentSelected(document) },
                        )
                    }
                    item { Spacer(Modifier.height(92.dp)) }
                }
            }
            FloatingActionButton(
                onClick = onNewDocument,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            ) {
                Text("+", fontSize = 28.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}

@Composable
private fun LibraryHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(CoreBlue),
            contentAlignment = Alignment.Center,
        ) {
            Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 21.sp)
        }
        Column(Modifier.padding(start = 14.dp)) {
            Text(
                text = "Slate",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Private notes and documents",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DocumentRow(
    document: Document,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (selected) 3.dp else 0.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (document.isPinned) {
                    Box(Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                    Spacer(Modifier.width(9.dp))
                }
                Text(
                    text = DocumentTitlePolicy.displayTitle(document.title, document.body),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = document.body.lineSequence().firstOrNull().orEmpty().ifBlank { "Empty document" },
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = document.updatedLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun Editor(
    document: Document,
    onDocumentChange: (Document) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 1.dp,
    ) {
        Column(Modifier.fillMaxSize()) {
            EditorHeader(onBack)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 20.dp),
            ) {
                BasicTextField(
                    value = document.title,
                    onValueChange = {
                        onDocumentChange(document.copy(title = it, updatedLabel = "Just now"))
                    },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { field ->
                        Box {
                            if (document.title.isBlank()) {
                                Text(
                                    "Untitled",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                FormatToolbar(Modifier.padding(vertical = 18.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                BasicTextField(
                    value = document.body,
                    onValueChange = {
                        onDocumentChange(document.copy(body = it, updatedLabel = "Just now"))
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { field ->
                        Box(Modifier.padding(top = 18.dp)) {
                            if (document.body.isBlank()) {
                                Text(
                                    "Start writing…",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun EditorHeader(onBack: (() -> Unit)?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = backIcon,
                    contentDescription = "Back to documents",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            Spacer(Modifier.width(8.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(
                text = "Document",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Saved on this device",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = {}) {
            Text(
                text = "•••",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FormatToolbar(modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        listOf("B", "I", "H1", "List", "Check").forEach { label ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clickable { },
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                )
            }
        }
    }
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
