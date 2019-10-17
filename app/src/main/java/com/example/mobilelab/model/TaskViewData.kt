package com.example.mobilelab.model

import android.graphics.drawable.ColorDrawable

data class TaskViewData(
    var category: String,
    var color: ColorDrawable?,
    var name: String?,
    var description: String?,
    var state: Boolean?,
    var isTask: Boolean
)