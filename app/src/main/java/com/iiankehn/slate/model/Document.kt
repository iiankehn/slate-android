package com.iiankehn.slate.model

data class Document(
    val id: String,
    val title: String,
    val body: RichTextDocument,
    val updatedLabel: String,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val folder: String = "",
    val tags: Set<String> = emptySet(),
    val updatedAtEpochMillis: Long = System.currentTimeMillis(),
    val wordProcessingDocument: WordProcessingDocument? = null,
    val experience: DocumentExperience = DocumentExperience.Adaptive,
)

enum class DocumentExperience { Adaptive, Notes, Forge }
