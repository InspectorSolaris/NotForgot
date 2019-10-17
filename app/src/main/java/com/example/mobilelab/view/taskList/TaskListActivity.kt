package com.example.mobilelab.view.taskList

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R

import kotlinx.android.synthetic.main.activity_tasks_list.*

class TaskListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {

        }
    }

}
