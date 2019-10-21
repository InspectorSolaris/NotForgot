package com.example.mobilelab.presenter.login

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.view.login.LoginInterface

class LoginPresenter(
    private var loginView: LoginInterface?
) {

    private val context = loginView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )

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
            sharedPreferencesHandler.saveString(
                context.getString(R.string.shared_preferences_user_key),
                email
            )
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