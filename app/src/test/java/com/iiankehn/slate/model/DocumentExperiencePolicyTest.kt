package com.iiankehn.slate.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DocumentExperiencePolicyTest {
    @Test
    fun `plain documents remain adaptive and present as notes`() {
        val wordDocument = WordProcessingDocument(id = "doc", title = "Draft")
        val document = Document("doc", "Draft", RichTextDocument.plain("Hello"), "Now", wordProcessingDocument = wordDocument)

        assertEquals(DocumentExperience.Adaptive, DocumentExperiencePolicy.resolve(document.experience, wordDocument))
        assertEquals(DocumentExperience.Notes, DocumentExperiencePolicy.effective(document))
    }

    @Test
    fun `page layout promotes an adaptive document to Forge`() {
        val wordDocument = WordProcessingDocument(
            id = "doc",
            title = "Report",
            sections = listOf(DocumentSection(page = PageSetup(orientation = PageOrientation.Landscape))),
        )

        assertEquals(DocumentExperience.Forge, DocumentExperiencePolicy.resolve(DocumentExperience.Adaptive, wordDocument))
    }

    @Test
    fun `document length alone never selects Forge`() {
        val longText = "word ".repeat(100_000)
        val wordDocument = WordProcessingDocument(
            id = "doc",
            title = "Journal",
            sections = listOf(DocumentSection(blocks = listOf(ParagraphBlock(runs = listOf(TextRun(longText))))),
        )

        assertEquals(DocumentExperience.Adaptive, DocumentExperiencePolicy.resolve(DocumentExperience.Adaptive, wordDocument))
    }

    @Test
    fun `Forge selection is sticky after advanced structure is removed`() {
        val simple = WordProcessingDocument(id = "doc", title = "Report")

        assertEquals(DocumentExperience.Forge, DocumentExperiencePolicy.resolve(DocumentExperience.Forge, simple))
    }
}
