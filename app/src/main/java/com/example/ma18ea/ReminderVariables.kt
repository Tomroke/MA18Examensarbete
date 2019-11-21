package com.example.ma18ea

class ReminderVariables(
    var uid:         Int = 0,
    var title:       String = "newTitle",
    var doneTime:    Long = 0,
    var totalTime:   Long = 0,
    var days:        ArrayList<String> = mutableListOf("Select Days") as ArrayList<String>,
    var description: String = "Description") {}