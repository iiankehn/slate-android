package com.iiankehn.slate.model

data class Document(
    val id: String,
    val title: String,
    val body: RichTextDocument,
    val updatedLabel: String,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val updatedAtEpochMillis: Long = System.currentTimeMillis(),
)
