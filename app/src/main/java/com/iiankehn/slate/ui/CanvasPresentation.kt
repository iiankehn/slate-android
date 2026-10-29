package com.iiankehn.slate.ui

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentExperience
import com.iiankehn.slate.model.DocumentExperiencePolicy

// October 2026 is the final Notes release. Keep the implementation available,
// while subsequent development builds use a single word-processing workspace.
internal const val NOTES_EXPERIENCE_ENABLED = false

internal fun usesContinuousCanvas(document: Document, notesEnabled: Boolean = NOTES_EXPERIENCE_ENABLED): Boolean =
    notesEnabled && DocumentExperiencePolicy.effective(document) == DocumentExperience.Notes
