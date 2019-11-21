package com.example.ma18ea.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class FragCommunicator : ViewModel(){

    val days = MutableLiveData<Any>()

    fun setDays(msg: String){
        days.value = msg
    }

}