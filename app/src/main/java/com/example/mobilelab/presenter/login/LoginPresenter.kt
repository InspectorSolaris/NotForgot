package com.example.mobilelab.presenter.login

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.view.login.LoginInterface

class LoginPresenter(
    private var loginView: LoginInterface?
) {

    private val context = loginView as Context
    private val repository = Repository()
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )

    fun onCreate() {
        val token = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_user_token)
        )
        val nullToken = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_null_token)
        )

        if(token != nullToken) {
            loginView?.onSuccessLogin()
        }
    }

    fun onDestroy() {
        loginView = null
    }

    fun onLoginButtonClick(
        email: String,
        password: String
    ) {
        repository.loginUser(
            UserLoginForm(email, password),
            { _, _ ->
                Toast
                    .makeText(
                        context,
                        R.string.login_toast_on_failed,
                        Toast.LENGTH_LONG
                    )
                    .show()
            },
            { _, response ->
                if(response.body() != null)
                {
                    sharedPreferencesHandler.saveString(
                        context.getString(R.string.shared_preferences_user_token),
                        response.body()!!.api_token
                    )

                    loginView?.onSuccessLogin()
                } else {
                    Toast
                        .makeText(
                            context,
                            response.message(),
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
        )
    }

    fun onRegistrationButtonClick() {
        loginView?.onRegistrationButtonClick()
    }

}