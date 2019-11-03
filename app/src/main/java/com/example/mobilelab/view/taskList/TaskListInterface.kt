package com.example.mobilelab.view.taskList

import android.content.Intent

interface TaskListInterface {

    fun initRecyclerView()

    fun runLottieAnimation(
        duration: Long
    )

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    )

    fun finish()

}