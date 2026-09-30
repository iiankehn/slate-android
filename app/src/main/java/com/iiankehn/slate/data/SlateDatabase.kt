package com.iiankehn.slate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DocumentEntity::class, RichTextRangeEntity::class, RecoveryEntryEntity::class],
    version = 1,
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
            ).build().also { instance = it }
        }
    }
}
