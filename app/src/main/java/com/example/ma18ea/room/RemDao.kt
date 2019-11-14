package com.example.ma18ea.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ma18ea.ReminderVariables

@Dao
interface RemDao {
    @Query("SELECT * FROM RemEntity")
    fun getAll(): List<RemEntity>

    @Query("SELECT * FROM RemEntity WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<RemEntity>

    @Query("SELECT * FROM RemEntity WHERE title LIKE :first ")
    fun findByName(first: String): RemEntity

    @Insert
    fun insertAll(vararg remEntities: RemEntity)

    @Delete
    fun delete(remEntity: RemEntity)
}
