package com.example.ma18ea.room

import android.util.Log
import androidx.room.TypeConverter


class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromList(data: ArrayList<String>): String {
            var string = ""
            for(str in data){
                string +=
                    if (str == data.last()){
                    (str)
                    }

                    else{
                    ("$str:")
                    }
            }
            return string
        }

        @TypeConverter
        @JvmStatic
        fun toList(data: String): ArrayList<String> {
            var splitStrings: ArrayList<String> = ArrayList()
            return if (!data.endsWith("y") || data.endsWith("s") && !data.contains(":")){
                splitStrings.add(data)
                splitStrings
            }else{
                splitStrings = data.split(":") as ArrayList<String>
                splitStrings
            }
        }
    }
}
