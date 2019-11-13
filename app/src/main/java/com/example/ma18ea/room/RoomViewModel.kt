package com.example.ma18ea.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class RoomViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: RoomRepository
    // LiveData gives us updated words when they change.
    val allWords: LiveData<List<RoomEntity>>

    init {
        val roomDao = RoomsDatabase.getDatabase(application, viewModelScope).roomDao()
        repository = RoomRepository(roomDao)
        allWords = repository.allWords
    }

    fun insert(word: RoomEntity) = viewModelScope.launch {
        repository.insert(word)
    }
}