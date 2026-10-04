package com.iiankehn.slate.model

/**
 * Keeps document creation effortless while preserving advanced Forge features once they are used.
 * Length never changes a document's experience; only its structure or an explicit user choice can.
 */
object DocumentExperiencePolicy {
    fun resolve(
        current: DocumentExperience,
        document: WordProcessingDocument,
    ): DocumentExperience = when {
        current == DocumentExperience.Forge || requiresForge(document) -> DocumentExperience.Forge
        else -> current
    }

    fun effective(document: Document): DocumentExperience = when {
        document.experience == DocumentExperience.Forge -> DocumentExperience.Forge
        document.wordProcessingDocument?.let(::requiresForge) == true -> DocumentExperience.Forge
        else -> DocumentExperience.Notes
    }

    fun requiresForge(document: WordProcessingDocument): Boolean =
        document.sections.size > 1 || document.sections.any { section ->
            section.page != PageSetup() ||
                section.header.isNotEmpty() ||
                section.footer.isNotEmpty() ||
                section.start != SectionStart.Continuous ||
                section.blocks.any { block ->
                    block is TableBlock ||
                        block is ParagraphBlock && block.style.pageBreakBefore
                }
        }
}
