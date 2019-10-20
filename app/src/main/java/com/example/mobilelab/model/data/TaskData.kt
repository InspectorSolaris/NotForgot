package com.example.mobilelab.model.data

import android.graphics.drawable.ColorDrawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskData(
    var category: String,
    var color: ColorDrawable?,
    var name: String?,
    var description: String?,
    var state: Boolean?,
    var isTask: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)