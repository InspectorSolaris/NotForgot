package com.example.mobilelab.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskData(
    var category: String,
    var taskOwner: String?,
    var color: String?,
    var title: String?,
    var description: String?,
    var priority: Int?,
    var state: Boolean?,
    var isTask: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)