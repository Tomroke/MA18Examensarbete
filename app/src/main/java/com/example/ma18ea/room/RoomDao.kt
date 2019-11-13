package com.example.ma18ea.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: RoomEntity)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}