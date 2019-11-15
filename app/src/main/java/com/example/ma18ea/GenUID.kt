package com.example.ma18ea

import java.util.*


class GenUID{

    fun generateUID(): Int {
        val currentTime = Calendar.getInstance().time

        return currentTime.time.toInt()
    }

}