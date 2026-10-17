package com.iiankehn.slate.ui

import com.iiankehn.slate.editing.FlatTextEditorAdapter
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentExperience
import com.iiankehn.slate.model.DocumentExperiencePolicy
import com.iiankehn.slate.model.DocumentSection
import com.iiankehn.slate.model.ImageBlock
import com.iiankehn.slate.model.PageOrientation
import com.iiankehn.slate.model.PageSetup
import com.iiankehn.slate.model.ParagraphBlock
import com.iiankehn.slate.model.RichTextDocument
import com.iiankehn.slate.model.TextRun
import com.iiankehn.slate.model.WordProcessingDocument
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CanvasPresentationTest {
    private fun document(word: WordProcessingDocument, experience: DocumentExperience = DocumentExperience.Adaptive) =
        Document("doc", "Draft", RichTextDocument(), "Now", wordProcessingDocument = word, experience = experience)

    @Test fun longNotesStayContinuousWithoutPaginationPromotion() {
        val word = WordProcessingDocument("doc", "Draft", sections = listOf(DocumentSection(
            blocks = listOf(ParagraphBlock(runs = listOf(TextRun("Long note\n".repeat(1000))))),
        )))
        assertTrue(usesContinuousCanvas(document(word)))
        assertFalse(usesContinuousCanvas(document(word, DocumentExperience.Forge)))
    }

    @Test fun pageLayoutSwitchKeepsTextAndStickyForgeChoice() {
        val word = WordProcessingDocument("doc", "Draft", sections = listOf(DocumentSection(
            blocks = listOf(ParagraphBlock(runs = listOf(TextRun("Preserve this note")))),
        )))
        val editor = FlatTextEditorAdapter(word)
        assertTrue(usesContinuousCanvas(document(editor.document)))
        editor.updatePageSetup(PageSetup(orientation = PageOrientation.Landscape))
        val experience = DocumentExperiencePolicy.resolve(DocumentExperience.Adaptive, editor.document)
        assertFalse(usesContinuousCanvas(document(editor.document, experience)))
        assertEquals("Preserve this note", editor.state().text)
        editor.undo()
        assertEquals("Preserve this note", editor.state().text)
        assertFalse(usesContinuousCanvas(document(editor.document, experience)))
    }

    @Test fun imageNotesUseContinuousCanvas() {
        val word = WordProcessingDocument("doc", "Draft", sections = listOf(DocumentSection(
            blocks = listOf(ParagraphBlock(), ImageBlock(id = "image-1", sourceUri = "file:///image.png")),
        )))
        assertEquals(DocumentExperience.Notes, DocumentExperiencePolicy.effective(document(word)))
        assertTrue(usesContinuousCanvas(document(word)))
    }
}
