package com.example.mobilelab.view.taskList

import android.content.Intent

interface TaskListInterface {

    fun initRecyclerView()

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    )

    fun finish()

}