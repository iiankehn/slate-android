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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentTitlePolicy
import com.iiankehn.slate.ui.theme.CoreBlue
import com.iiankehn.slate.ui.theme.Midnight
import com.iiankehn.slate.ui.theme.SlateSurfaceRaised
import com.iiankehn.slate.ui.theme.SlateTextMuted

private val starterDocuments = listOf(
    Document("welcome", "Welcome to Slate", "A calm place for notes, drafts, and complete documents.\n\nEverything starts on your device.", "Just now", true),
    Document("ideas", "Project ideas", "Build the smallest useful version first.\nKeep the editor fast.\nRespect the writer's privacy.", "12 min ago"),
    Document("meeting", "Meeting notes", "Agenda\n\n• Current work\n• Decisions\n• Next steps", "Yesterday"),
)

private enum class CompactDestination { Library, Editor }

@Composable
fun SlateApp() {
    var selected by remember { mutableStateOf(starterDocuments.first()) }
    var destination by remember { mutableStateOf(CompactDestination.Library) }
    var documents by remember { mutableStateOf(starterDocuments) }

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
                        onDocumentSelected = { selected = it },
                        onNewDocument = {
                            val newDocument = Document("new-${documents.size}", "", "", "Just now")
                            documents = listOf(newDocument) + documents
                            selected = newDocument
                        },
                        modifier = Modifier.width(360.dp).fillMaxHeight(),
                    )
                    Editor(
                        document = selected,
                        onDocumentChange = { changed ->
                            selected = changed
                            documents = documents.map { if (it.id == changed.id) changed else it }
                        },
                        modifier = Modifier.weight(1f),
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
                        val newDocument = Document("new-${documents.size}", "", "", "Just now")
                        documents = listOf(newDocument) + documents
                        selected = newDocument
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            ) {
                items(documents, key = { it.id }) { document ->
                    val selected = selectedId == document.id
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (selected) CoreBlue.copy(alpha = 0.23f) else Color.Transparent)
                            .clickable { onDocumentSelected(document) }
                            .padding(horizontal = 14.dp, vertical = 13.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (document.isPinned) {
                                Box(Modifier.size(7.dp).background(CoreBlue, CircleShape))
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(
                                DocumentTitlePolicy.displayTitle(document.title, document.body),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                            )
                        }
                        Text(document.updatedLabel, color = SlateTextMuted, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Editor(
    document: Document,
    onDocumentChange: (Document) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
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
            Text("Saved on device", color = SlateTextMuted, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text("•••", color = SlateTextMuted, fontWeight = FontWeight.Bold)
        }

        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 20.dp)) {
                BasicTextField(
                    value = document.title,
                    onValueChange = { onDocumentChange(document.copy(title = it, updatedLabel = "Just now")) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 32.sp, fontWeight = FontWeight.Bold),
                    cursorBrush = SolidColor(CoreBlue),
                    decorationBox = { field ->
                        Box {
                            if (document.title.isBlank()) Text("Untitled", color = SlateTextMuted, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 18.dp),
                ) {
                    listOf("B", "I", "H1", "List", "Check").forEach { label ->
                        Surface(
                            color = SlateSurfaceRaised,
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                BasicTextField(
                    value = document.body,
                    onValueChange = { onDocumentChange(document.copy(body = it, updatedLabel = "Just now")) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, lineHeight = 29.sp),
                    cursorBrush = SolidColor(CoreBlue),
                    decorationBox = { field ->
                        Box(Modifier.padding(top = 18.dp)) {
                            if (document.body.isBlank()) Text("Start writing…", color = SlateTextMuted, fontSize = 18.sp)
                            field()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
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
