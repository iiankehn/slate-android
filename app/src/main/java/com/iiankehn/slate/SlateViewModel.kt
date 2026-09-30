package com.iiankehn.slate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iiankehn.slate.data.DocumentRepository
import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.DocumentTitlePolicy
import com.iiankehn.slate.model.RichTextDocument
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SlateUiState(
    val documents: List<Document> = emptyList(),
    val loading: Boolean = true,
    val savingDocumentIds: Set<String> = emptySet(),
)

class SlateViewModel(
    private val repository: DocumentRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(SlateUiState())
    val uiState: StateFlow<SlateUiState> = mutableUiState.asStateFlow()

    private val pendingSaves = mutableMapOf<String, Job>()
    private val saveGenerations = mutableMapOf<String, Int>()

    init {
        viewModelScope.launch {
            repository.initialize(starterDocuments)
            repository.documents.collect { storedDocuments ->
                val localDocuments = mutableUiState.value.documents.associateBy(Document::id)
                val merged = storedDocuments.map { stored ->
                    if (pendingSaves.containsKey(stored.id)) localDocuments[stored.id] ?: stored else stored
                } + localDocuments.values.filter { local ->
                    pendingSaves.containsKey(local.id) && storedDocuments.none { it.id == local.id }
                }
                mutableUiState.value = SlateUiState(
                    documents = merged.sortedForLibrary(),
                    loading = false,
                    savingDocumentIds = mutableUiState.value.savingDocumentIds,
                )
            }
        }
    }

    fun createDocument(): Document {
        val document = Document(
            id = UUID.randomUUID().toString(),
            title = "",
            body = RichTextDocument(),
            updatedLabel = "Just now",
        )
        updateLocal(document)
        scheduleSave(document, delayMillis = 0)
        return document
    }

    fun updateDocument(document: Document) {
        val changed = document.copy(
            updatedLabel = "Just now",
            updatedAtEpochMillis = nextTimestamp(document),
        )
        updateLocal(changed)
        viewModelScope.launch { repository.checkpoint(changed) }
        scheduleSave(changed, journalFirst = false)
    }

    fun duplicateDocument(source: Document): Document {
        val duplicate = source.copy(
            id = UUID.randomUUID().toString(),
            title = "${DocumentTitlePolicy.displayTitle(source.title, source.body.text)} copy",
            updatedLabel = "Just now",
            isPinned = false,
            isArchived = false,
            updatedAtEpochMillis = nextTimestamp(source),
        )
        updateLocal(duplicate)
        scheduleSave(duplicate, delayMillis = 0)
        return duplicate
    }

    fun togglePin(source: Document): Document {
        val changed = source.copy(
            isPinned = !source.isPinned,
            updatedLabel = "Just now",
            updatedAtEpochMillis = nextTimestamp(source),
        )
        updateLocal(changed)
        scheduleSave(changed, delayMillis = 0)
        return changed
    }

    fun toggleArchive(source: Document): Document {
        val changed = source.copy(
            isArchived = !source.isArchived,
            updatedLabel = "Just now",
            updatedAtEpochMillis = nextTimestamp(source),
        )
        updateLocal(changed)
        scheduleSave(changed, delayMillis = 0)
        return changed
    }

    fun deleteDocument(source: Document) {
        pendingSaves.remove(source.id)?.cancel()
        saveGenerations.remove(source.id)
        mutableUiState.update { state ->
            state.copy(
                documents = state.documents.filterNot { it.id == source.id },
                savingDocumentIds = state.savingDocumentIds - source.id,
            )
        }
        viewModelScope.launch { repository.delete(source) }
    }

    private fun updateLocal(document: Document) {
        mutableUiState.update { state ->
            val exists = state.documents.any { it.id == document.id }
            val documents = if (exists) {
                state.documents.map { if (it.id == document.id) document else it }
            } else {
                listOf(document) + state.documents
            }
            state.copy(documents = documents.sortedForLibrary(), loading = false)
        }
    }

    private fun scheduleSave(
        document: Document,
        delayMillis: Long = AUTOSAVE_DELAY_MILLIS,
        journalFirst: Boolean = true,
    ) {
        pendingSaves.remove(document.id)?.cancel()
        val generation = (saveGenerations[document.id] ?: 0) + 1
        saveGenerations[document.id] = generation
        mutableUiState.update { state ->
            state.copy(savingDocumentIds = state.savingDocumentIds + document.id)
        }
        pendingSaves[document.id] = viewModelScope.launch {
            try {
                if (delayMillis > 0) delay(delayMillis)
                repository.save(document, journalFirst = journalFirst)
            } finally {
                if (saveGenerations[document.id] == generation) {
                    pendingSaves.remove(document.id)
                    saveGenerations.remove(document.id)
                    mutableUiState.update { state ->
                        state.copy(savingDocumentIds = state.savingDocumentIds - document.id)
                    }
                }
            }
        }
    }

    class Factory(
        private val repository: DocumentRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(SlateViewModel::class.java))
            return SlateViewModel(repository) as T
        }
    }

    private companion object {
        const val AUTOSAVE_DELAY_MILLIS = 450L

        val starterDocuments = listOf(
            Document(
                "welcome",
                "Welcome to Slate",
                RichTextDocument.plain("A calm place for notes, drafts, and complete documents.\n\nEverything starts on your device."),
                "Just now",
                true,
            ),
            Document(
                "ideas",
                "Project ideas",
                RichTextDocument.plain("Build the smallest useful version first.\nKeep the editor fast.\nRespect the writer's privacy."),
                "12 min ago",
            ),
            Document(
                "meeting",
                "Meeting notes",
                RichTextDocument.plain("Agenda\n\n• Current work\n• Decisions\n• Next steps"),
                "Yesterday",
            ),
        )
    }
}

private fun List<Document>.sortedForLibrary(): List<Document> = sortedWith(
    compareBy<Document> { it.isArchived }
        .thenByDescending { it.isPinned }
        .thenByDescending(Document::updatedAtEpochMillis),
)

private fun nextTimestamp(document: Document): Long = maxOf(
    System.currentTimeMillis(),
    document.updatedAtEpochMillis + 1,
)
