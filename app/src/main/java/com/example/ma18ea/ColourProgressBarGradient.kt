package com.example.ma18ea

import android.graphics.Color
import android.util.Log

class ColourProgressBarGradient {

    fun getColour (progress: Int): Int {
        return when {
            progress <= 25 -> Color.BLACK
            progress <= 50 -> Color.BLUE
            progress <= 75 -> Color.RED
            progress <= 75 -> Color.MAGENTA
            else -> Color.GRAY
        }
    }

}