package com.example.ma18ea

import java.text.DecimalFormat

class Calculation {

    private lateinit var timeInString: String
    private lateinit var decimalCut:DecimalFormat

    fun ofProgressBar (doneTime: Int, totalTime: Int): Double {
        return if (doneTime != 0) {
            val onePercent = totalTime / 100.0
            doneTime / onePercent
        }else{
            0.0
        }
    }

    fun ofTimeInHours(doneTime: Int, totalTime: Int): String {
        decimalCut = DecimalFormat("#0.##")
        val timeHours:Double = totalTime / 60.0
        val timeDone:Double = doneTime / 60.0
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

    fun toMin (milli: Long): Int{
        return milli.toInt() / 60000
    }

    fun newRemainingTime (totalTime: Long, previousTime: Long, currentDoneTime: Long):Long{
        return (totalTime - previousTime) - currentDoneTime
    }

}