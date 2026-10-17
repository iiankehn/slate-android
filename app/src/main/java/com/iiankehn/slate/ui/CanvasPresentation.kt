package com.iiankehn.slate.ui

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentExperience
import com.iiankehn.slate.model.DocumentExperiencePolicy
import com.iiankehn.slate.model.ParagraphBlock

/** Object-bearing notes keep the existing object editor until its text offsets are unified. */
internal fun usesContinuousCanvas(document: Document): Boolean =
    DocumentExperiencePolicy.effective(document) == DocumentExperience.Notes &&
        document.wordProcessingDocument?.sections?.all { section -> section.blocks.all { it is ParagraphBlock } } != false
