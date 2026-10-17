package com.iiankehn.slate.ui

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.RichTextDocument
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryViewTest {
    private val document = Document("id", "Project", RichTextDocument("Private draft"), "Now", folder = "Work", tags = setOf("review", "writing"))

    @Test fun folderTagAndTextFiltersCombine() {
        assertTrue(matchesLibraryView(document, LibraryFilter.Documents, " DRAFT ", "Work", "review"))
        assertFalse(matchesLibraryView(document, LibraryFilter.Documents, "draft", "Personal", "review"))
        assertFalse(matchesLibraryView(document, LibraryFilter.Documents, "draft", "Work", "view"))
        assertTrue(matchesLibraryView(document, LibraryFilter.Documents, "WRITING", null, null))
    }

    @Test fun archiveAndTrashRemainSeparateFromActiveDocuments() {
        val archived = document.copy(isArchived = true, isFavorite = true)
        assertFalse(matchesLibraryView(archived, LibraryFilter.Documents, "", null, null))
        assertTrue(matchesLibraryView(archived, LibraryFilter.Archive, "", null, null))
        assertTrue(matchesLibraryView(archived, LibraryFilter.Favorites, "", null, null))
        val trashed = archived.copy(isDeleted = true)
        assertFalse(matchesLibraryView(trashed, LibraryFilter.Favorites, "", null, null))
        assertFalse(matchesLibraryView(trashed, LibraryFilter.Archive, "", null, null))
        assertTrue(matchesLibraryView(trashed, LibraryFilter.Trash, "", "Work", "review"))
    }

    @Test fun tagEditsTrimDeduplicateAndAllowClearing() {
        assertEquals(setOf("draft", "review"), parseDocumentTags(" draft, review, draft, , "))
        assertEquals(emptySet<String>(), parseDocumentTags(" , "))
    }
}
