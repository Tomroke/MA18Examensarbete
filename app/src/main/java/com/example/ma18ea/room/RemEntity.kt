package com.example.ma18ea.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "done_time") val doneTime: Long?,
    @ColumnInfo(name = "total_time") val totalTime: Long?,
    @ColumnInfo(name = "days") val days: String?,
    @ColumnInfo(name = "description") val description: String?
)