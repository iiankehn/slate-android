package com.iiankehn.slate.ui

import com.iiankehn.slate.model.Document

internal enum class LibraryFilter { Documents, Favorites, Archive, Trash }

internal fun matchesLibraryView(
    document: Document,
    filter: LibraryFilter,
    query: String,
    folder: String?,
    tag: String?,
): Boolean {
    val inSection = when (filter) {
        LibraryFilter.Documents -> !document.isDeleted && !document.isArchived
        LibraryFilter.Favorites -> document.isFavorite && !document.isDeleted
        LibraryFilter.Archive -> document.isArchived && !document.isDeleted
        LibraryFilter.Trash -> document.isDeleted
    }
    if (!inSection || (folder != null && document.folder != folder) || (tag != null && tag !in document.tags)) return false
    val search = query.trim()
    return search.isEmpty() || listOf(document.title, document.body.text, document.folder, document.tags.joinToString(" "))
        .joinToString(" ").contains(search, ignoreCase = true)
}

internal fun parseDocumentTags(value: String): Set<String> =
    value.split(',').map(String::trim).filter(String::isNotEmpty).toSet()
