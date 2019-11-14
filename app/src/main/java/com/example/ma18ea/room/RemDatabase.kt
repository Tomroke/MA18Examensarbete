package com.example.ma18ea.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [RemEntity::class], version = 1)
abstract class RemDatabase : RoomDatabase() {
    abstract fun remDao(): RemDao

    companion object {
        @Volatile private var INSTANCE: RemDatabase? = null

        fun getInstance(context: Context): RemDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RemDatabase::class.java, "app.db").build()
    }

}
