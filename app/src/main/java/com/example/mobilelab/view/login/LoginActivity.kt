package com.example.mobilelab.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.presenter.login.LoginPresenter
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity :
    AppCompatActivity(),
    LoginInterface {

    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter(this)

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

    override fun startActivity(
        intent: Intent?
    ) {
        super.startActivity(intent)
    }

}
