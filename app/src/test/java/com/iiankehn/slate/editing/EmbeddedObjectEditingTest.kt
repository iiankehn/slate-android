package com.iiankehn.slate.editing

import com.iiankehn.slate.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmbeddedObjectEditingTest {
    private val image = ImageBlock("image", "content://picture", "Private caption")
    private val table = TableBlock("table", listOf(TableRow(listOf(TableCell(listOf(paragraph("cell", "Cell data")))))))
    private fun paragraph(id: String, text: String) = ParagraphBlock(id, listOf(TextRun(text)))
    private fun document() = WordProcessingDocument("doc", "Draft", listOf(DocumentSection(blocks = listOf(
        paragraph("before", "Before"), image, table, paragraph("after", "After"),
    ))))

    @Test fun textFieldProjectionAndEngineUseTheSameOffsets() {
        val editor = FlatTextEditorAdapter(document())
        assertEquals("Before\nAfter", editor.state().text)
        assertEquals(editor.state().text, editor.editableBody().text)
        assertTrue(editor.legacyBody().text.contains("Private caption"))
        assertTrue(editor.legacyBody().text.contains("Cell data"))

        editor.replace("Before\nAfter!", 13, 13)
        editor.toggle(CharacterFormat.Bold, 7, 12)

        assertEquals("Before\nAfter!", editor.editableBody().text)
        val after = editor.document.sections.single().blocks.filterIsInstance<ParagraphBlock>().last()
        assertTrue(after.runs.first().style.bold)
        assertEquals(image, editor.document.sections.single().blocks.filterIsInstance<ImageBlock>().single())
        assertEquals(table, editor.document.sections.single().blocks.filterIsInstance<TableBlock>().single())
        val bold = editor.editableBody().ranges.single { it.style == RichTextStyle.Bold }
        assertEquals(7, bold.start)
        assertEquals(12, bold.end)
    }

    @Test fun replacingTextAcrossObjectsPreservesThemAndSupportsUndoRedo() {
        val editor = FlatTextEditorAdapter(document())
        editor.replace("Replacement", 11, 11)
        assertEquals(listOf(image, table), editor.document.sections.single().blocks.filterNot { it is ParagraphBlock })
        assertEquals("Replacement", editor.editableBody().text)
        assertEquals("Before\nAfter", editor.undo().text)
        assertEquals(listOf(image, table), editor.document.sections.single().blocks.filterNot { it is ParagraphBlock })
        assertEquals("Replacement", editor.redo().text)
        editor.deleteObject(image.id)
        assertTrue(editor.document.sections.single().blocks.none { it.id == image.id })
        editor.undo()
        assertEquals(image, editor.document.sections.single().blocks.filterIsInstance<ImageBlock>().single())
    }

    @Test fun crossSectionTextReplacementKeepsEmbeddedObjects() {
        val word = WordProcessingDocument("doc", "Draft", listOf(
            DocumentSection(blocks = listOf(paragraph("before", "Before"), image)),
            DocumentSection(blocks = listOf(table, paragraph("after", "After"))),
        ))
        val editor = FlatTextEditorAdapter(word)
        editor.replace("Merged", 6, 6)
        assertEquals("Merged", editor.editableBody().text)
        assertEquals(listOf(image, table), editor.document.sections.flatMap { it.blocks }.filterNot { it is ParagraphBlock })
        editor.undo()
        assertEquals(2, editor.document.sections.size)
        assertEquals("Before\nAfter", editor.state().text)
    }
}
