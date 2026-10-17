package com.iiankehn.slate.ui

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentExperience
import com.iiankehn.slate.model.DocumentExperiencePolicy

internal fun usesContinuousCanvas(document: Document): Boolean =
    DocumentExperiencePolicy.effective(document) == DocumentExperience.Notes
