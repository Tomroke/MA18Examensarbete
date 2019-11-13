package com.example.ma18ea.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [(RoomEntity::class)], version = 2, exportSchema = false)
abstract class RoomsDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.roomDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: RoomDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = RoomEntity("Hello", "title1")
            wordDao.insert(word)
            word = RoomEntity("World!", "title2")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RoomsDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RoomsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomsDatabase::class.java,
                    "word_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigrationFrom()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // https://developer.android.com/reference/android/arch/persistence/room/ColumnInfo

        database.execSQL()
    }
}
