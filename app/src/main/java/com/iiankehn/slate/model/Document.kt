package com.iiankehn.slate.model

data class Document(
    val id: String,
    val title: String,
    val body: String,
    val updatedLabel: String,
    val isPinned: Boolean = false,
)
