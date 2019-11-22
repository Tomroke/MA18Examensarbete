package com.example.ma18ea

import java.text.DecimalFormat

class Calculation {

    private lateinit var timeInString: String
    private lateinit var decimalCut:DecimalFormat

    fun ofProgressBar (doneTime: Long?, totalTime: Long?): Double {
        return if (toMin(doneTime) != 0) {
            val onePercent = toMin(totalTime) / 100.0
            toMin(doneTime) / onePercent
        }else{
            0.0
        }
    }

    fun ofTimeInHours(doneTime: Long?, totalTime: Long?): String {
        decimalCut = DecimalFormat("#0.##")
        val timeHours:Double = toMin(totalTime) / 60.0
        val timeDone:Double = toMin(doneTime) / 60.0
        return if(timeHours <= 1.0){
            timeInString = decimalCut.format(timeDone) + " hours out of " +  decimalCut.format(timeHours) + " hour"
            timeInString
        } else{
            timeInString = decimalCut.format(timeDone) + " hours out of " +  decimalCut.format(timeHours) + " set hours"
            timeInString
        }
    }

    fun toMilli (min: Int): Long{
        return min.toLong() * 60000
    }

    private fun toMin (milli: Long?): Int{
        return milli!!.toInt() / 60000
    }

}