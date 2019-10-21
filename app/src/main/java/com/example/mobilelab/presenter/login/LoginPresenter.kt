package com.example.mobilelab.presenter.login

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.view.login.LoginInterface

class LoginPresenter(
    private var loginView: LoginInterface?
) {

    fun onDestroy() {
        loginView = null
    }

    fun onLoginButtonClick(
        email: String,
        password: String
    ) {
        if (userIsRegistered(
                email,
                password
            )
        ) {
            loginView?.onSuccessLogin()
        } else {
            Toast
                .makeText(
                    loginView as Context,
                    R.string.login_toast_unable_to_login,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    fun onRegistrationButtonClick() {
        loginView?.onRegistrationButtonClick()
    }

    private fun userIsRegistered(
        email: String,
        password: String
    ): Boolean {
        return email.isNotEmpty()
                && password.isNotEmpty()
    }

}