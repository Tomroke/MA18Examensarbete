package com.example.ma18ea.room

import androidx.room.*
import com.example.ma18ea.ReminderVariables

@Dao
interface RemDao {
    @Query("SELECT * FROM RemEntity")
    fun getAll(): List<RemEntity>

    @Query("SELECT * FROM RemEntity WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<RemEntity>

    @Query("SELECT * FROM RemEntity WHERE uid LIKE :first ")
    fun findByID(first: Int): RemEntity

    @Update
    fun updateData(vararg remEntities: RemEntity)

    @Insert
    fun insertAll(vararg remEntities: RemEntity)

    @Delete
    fun delete(remEntity: RemEntity)
}
