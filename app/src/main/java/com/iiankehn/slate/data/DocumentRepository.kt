package com.iiankehn.slate.data

import com.iiankehn.slate.model.Document
import com.iiankehn.slate.model.RichTextDocument
import com.iiankehn.slate.model.RichTextRange
import com.iiankehn.slate.model.RichTextStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DocumentRepository(
    private val dao: SlateDao,
) {
    val documents: Flow<List<Document>> = dao.observeDocuments().map { rows ->
        rows.map(DocumentWithRanges::toModel)
    }

    suspend fun initialize(starterDocuments: List<Document>) {
        recoverInterruptedWrites()
        if (dao.documentCount() == 0) {
            starterDocuments.forEach { save(it, journalFirst = false) }
        }
    }

    suspend fun save(document: Document, journalFirst: Boolean = true) {
        val normalized = document.copy(body = document.body.normalized())
        if (journalFirst) checkpoint(normalized)
        dao.replaceDocument(normalized.toEntity(), normalized.toRangeEntities())
    }

    suspend fun checkpoint(document: Document) {
        val normalized = document.copy(body = document.body.normalized())
        dao.insertRecovery(normalized.toRecovery())
        dao.trimRecovery(normalized.id, RECOVERY_LIMIT)
    }

    suspend fun delete(document: Document) {
        dao.insertRecovery(
            document.copy(
                updatedAtEpochMillis = maxOf(System.currentTimeMillis(), document.updatedAtEpochMillis + 1),
            ).toRecovery(isDeletion = true),
        )
        dao.trimRecovery(document.id, RECOVERY_LIMIT)
        dao.deleteDocument(document.id)
    }

    private suspend fun recoverInterruptedWrites() {
        val current = dao.getDocuments().associateBy { it.document.id }
        val newestRecoveries = dao.getRecoveryEntries().distinctBy(RecoveryEntryEntity::documentId)
        newestRecoveries.forEach { recovery ->
            val stored = current[recovery.documentId]?.document
            if (!recovery.isDeletion && (stored == null || recovery.createdAtEpochMillis > stored.updatedAtEpochMillis)) {
                val recovered = recovery.toModel()
                dao.replaceDocument(recovered.toEntity(), recovered.toRangeEntities())
            }
        }
    }

    private companion object {
        const val RECOVERY_LIMIT = 30
    }
}

private fun Document.toEntity() = DocumentEntity(
    id = id,
    title = title,
    bodyText = body.text,
    updatedLabel = updatedLabel,
    isPinned = isPinned,
    isArchived = isArchived,
    updatedAtEpochMillis = updatedAtEpochMillis,
)

private fun Document.toRangeEntities() = body.normalized().ranges.map { range ->
    RichTextRangeEntity(
        documentId = id,
        style = range.style.name,
        start = range.start,
        end = range.end,
    )
}

private fun DocumentWithRanges.toModel() = Document(
    id = document.id,
    title = document.title,
    body = RichTextDocument(
        text = document.bodyText,
        ranges = ranges.mapNotNull { range ->
            runCatching {
                RichTextRange(
                    style = RichTextStyle.valueOf(range.style),
                    start = range.start,
                    end = range.end,
                )
            }.getOrNull()
        },
    ).normalized(),
    updatedLabel = document.updatedLabel,
    isPinned = document.isPinned,
    isArchived = document.isArchived,
    updatedAtEpochMillis = document.updatedAtEpochMillis,
)

private fun Document.toRecovery(isDeletion: Boolean = false) = RecoveryEntryEntity(
    documentId = id,
    title = title,
    bodyText = body.text,
    rangePayload = RangePayload.encode(body.ranges),
    updatedLabel = updatedLabel,
    isPinned = isPinned,
    isArchived = isArchived,
    isDeletion = isDeletion,
    createdAtEpochMillis = updatedAtEpochMillis,
)

private fun RecoveryEntryEntity.toModel() = Document(
    id = documentId,
    title = title,
    body = RichTextDocument(bodyText, RangePayload.decode(rangePayload)).normalized(),
    updatedLabel = updatedLabel,
    isPinned = isPinned,
    isArchived = isArchived,
    updatedAtEpochMillis = createdAtEpochMillis,
)

internal object RangePayload {
    fun encode(ranges: List<RichTextRange>): String = ranges.joinToString(";") { range ->
        "${range.style.name},${range.start},${range.end}"
    }

    fun decode(payload: String): List<RichTextRange> = payload
        .split(';')
        .mapNotNull { encoded ->
            val parts = encoded.split(',')
            if (parts.size != 3) return@mapNotNull null
            runCatching {
                RichTextRange(
                    style = RichTextStyle.valueOf(parts[0]),
                    start = parts[1].toInt(),
                    end = parts[2].toInt(),
                )
            }.getOrNull()
        }
}
