package com.example.mobilelab.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.presenter.login.LoginPresenter
import com.example.mobilelab.view.registration.RegistrationActivity
import com.example.mobilelab.view.taskList.TaskListActivity
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity :
    AppCompatActivity(),
    LoginInterface {

    private val loginPresenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val repository = Repository(this)

        loginButton.setOnClickListener {
            loginPresenter.onLoginButtonClick(
                userEmail.text.toString(),
                userPassword.text.toString()
            )
        }

        registrationButton.setOnClickListener {
            loginPresenter.onRegistrationButtonClick()
        }
    }

    override fun onDestroy() {
        loginPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onSuccessLogin() {
        val intent = Intent(this, TaskListActivity::class.java)

        startActivity(intent)
    }

    override fun onRegistrationButtonClick() {
        val intent = Intent(this, RegistrationActivity::class.java)

        startActivity(intent)
    }

}
