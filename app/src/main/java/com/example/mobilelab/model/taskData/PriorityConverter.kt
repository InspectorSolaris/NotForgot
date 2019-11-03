package com.example.mobilelab.model.taskData

import androidx.room.TypeConverter

class PriorityConverter {

    @TypeConverter
    fun fromPriority(
        priority: Priority
    ): String {
        return "${priority.id}///${priority.name}///${priority.color}"
    }

    @TypeConverter
    fun stringToPriority(
        string: String
    ): Priority {
        val args = string.split("///")
        return Priority(args[0].toInt(), args[1], args[2])
    }

}