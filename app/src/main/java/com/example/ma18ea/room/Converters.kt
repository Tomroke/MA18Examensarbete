package com.example.ma18ea.room

import androidx.room.TypeConverter


class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromList(value: List<String>): String {
            var string = "";
            for(str in value){
                string += ("$str:")
            }
            return string
        }

        @TypeConverter
        @JvmStatic
        fun toList(value: String): List<String> {
            return value.split(":").map { it.trim() }
        }
    }
}
