package com.iiankehn.slate.data

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.RichTextDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryRestoreTest {
    @Test
    fun restoredContentSurvivesStartupWithNewerRecoveryHistory() = runBlocking {
        val dao = MemoryDao()
        val repository = DocumentRepository(dao)
        val future = System.currentTimeMillis() + 100_000
        val current = document("existing", "Current", future)
        repository.save(current)
        repository.checkpoint(current.copy(body = RichTextDocument("Unfinished edit"), updatedAtEpochMillis = future + 1))
        val unrelated = document("unrelated", "Keep me", 10)
        repository.save(unrelated)

        repository.restore(listOf(document("existing", "Restored", 1)))
        assertTrue(dao.rows.getValue("existing").document.updatedAtEpochMillis > future + 1)
        repository.initialize(emptyList())

        assertEquals("Restored", dao.rows.getValue("existing").document.bodyText)
        assertEquals("Keep me", dao.rows.getValue("unrelated").document.bodyText)
        assertEquals(2, dao.getRecoveryEntries("existing").size)
    }

    @Test
    fun invalidTimestampFailsBeforeAnyRestoreWrites() = runBlocking {
        val dao = MemoryDao()
        val repository = DocumentRepository(dao)
        val failure = runCatching {
            repository.restore(listOf(document("valid", "First", 1), document("invalid", "Second", Long.MAX_VALUE)))
        }.exceptionOrNull()
        assertTrue(failure is IllegalArgumentException)
        assertTrue(dao.rows.isEmpty())
    }

    private fun document(id: String, text: String, timestamp: Long) =
        Document(id, id, RichTextDocument(text), "Test", updatedAtEpochMillis = timestamp)

    private class MemoryDao : SlateDao() {
        val rows = linkedMapOf<String, DocumentWithRanges>()
        private val recovery = mutableListOf<RecoveryEntryEntity>()
        override fun observeDocuments(): Flow<List<DocumentWithRanges>> = flowOf(rows.values.toList())
        override suspend fun getDocuments(): List<DocumentWithRanges> = rows.values.toList()
        override suspend fun documentCount(): Int = rows.size
        override suspend fun upsertDocument(document: DocumentEntity) {
            rows[document.id] = DocumentWithRanges(document, emptyList())
        }
        override suspend fun deleteRanges(documentId: String) {
            rows[documentId]?.let { rows[documentId] = it.copy(ranges = emptyList()) }
        }
        override suspend fun insertRanges(ranges: List<RichTextRangeEntity>) {
            ranges.groupBy { it.documentId }.forEach { (id, values) ->
                rows[id]?.let { rows[id] = it.copy(ranges = values) }
            }
        }
        override suspend fun deleteDocument(documentId: String) { rows.remove(documentId) }
        override suspend fun insertRecovery(entry: RecoveryEntryEntity) { recovery.add(entry) }
        override suspend fun getRecoveryEntries(): List<RecoveryEntryEntity> =
            recovery.sortedByDescending { it.createdAtEpochMillis }
        override suspend fun getRecoveryEntries(documentId: String): List<RecoveryEntryEntity> =
            getRecoveryEntries().filter { it.documentId == documentId && !it.isDeletion }
        override suspend fun trimRecovery(documentId: String, keepCount: Int) = Unit
    }
}
