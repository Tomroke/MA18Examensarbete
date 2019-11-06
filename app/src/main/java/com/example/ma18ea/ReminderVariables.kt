package com.example.ma18ea

class ReminderVariables(
        var title:String = "Title",
        var doneTime:Int = 0,
        var totalTime:Int = 0,
        var days:ArrayList<String> = ArrayList(7),
        var description:String = "Description") {}