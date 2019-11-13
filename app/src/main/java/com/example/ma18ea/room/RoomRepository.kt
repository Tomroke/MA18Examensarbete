package com.example.ma18ea.room

import androidx.lifecycle.LiveData

class RoomRepository(private val wordDao: RoomDao) {

    val allWords: LiveData<List<RoomEntity>> = wordDao.getAlphabetizedWords()

    suspend fun insert(word: RoomEntity) {
        wordDao.insert(word)
    }
}