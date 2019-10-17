package com.example.mobilelab.view.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.view.taskList.TaskListActivity
import kotlinx.android.synthetic.main.content_registration.*

class RegistrationActivity :
    AppCompatActivity(),
    RegistrationInterface {

    private var registrationPresenter = RegistrationPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        registrationButton.setOnClickListener {
            registrationPresenter.onRegistrationButtonClick(
                userName.text.toString(),
                userEmail.text.toString(),
                userPassword.text.toString(),
                userPassword2.text.toString()
            )
        }

        loginButton.setOnClickListener {
            registrationPresenter.onLoginButtonClick()
        }
    }

    override fun onDestroy() {
        registrationPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onSuccessRegistration() {
        val intent = Intent(this, TaskListActivity::class.java)

        startActivity(intent)
    }

    override fun onLoginButtonClick() {
        finish()
    }

}
