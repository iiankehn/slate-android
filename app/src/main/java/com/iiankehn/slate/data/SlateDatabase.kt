package com.iiankehn.slate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [DocumentEntity::class, RichTextRangeEntity::class, RecoveryEntryEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class SlateDatabase : RoomDatabase() {
    abstract fun slateDao(): SlateDao

    companion object {
        @Volatile
        private var instance: SlateDatabase? = null

        fun getInstance(context: Context): SlateDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                SlateDatabase::class.java,
                "slate.db",
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build().also { instance = it }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE documents ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE documents ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE documents ADD COLUMN folder TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE documents ADD COLUMN tagsPayload TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE rich_text_ranges ADD COLUMN data TEXT")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN folder TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN tagsPayload TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE documents ADD COLUMN r2Payload TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN r2Payload TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE documents ADD COLUMN experience TEXT NOT NULL DEFAULT 'Adaptive'")
                database.execSQL("ALTER TABLE recovery_entries ADD COLUMN experience TEXT NOT NULL DEFAULT 'Adaptive'")
            }
        }
    }
}
