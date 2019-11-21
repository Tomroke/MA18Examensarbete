package com.example.ma18ea.room

import androidx.room.TypeConverter


class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromList(value: ArrayList<String>): String {
            var string = ""
            for(str in value){
                string +=
                    if (str == value.last()){
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
        fun toList(value: String): ArrayList<String> {
            return value.split(":") as ArrayList<String>
        }
    }
}
