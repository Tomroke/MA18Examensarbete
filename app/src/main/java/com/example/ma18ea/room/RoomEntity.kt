package com.example.ma18ea.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0

@Entity(tableName = "word_table")
class RoomEntity(@PrimaryKey
                 @ColumnInfo(name = "word") val word: String,
                 @ColumnInfo(name = "title") val title: String)
