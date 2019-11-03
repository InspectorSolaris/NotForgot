package com.example.mobilelab.presenter.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.view.login.LoginInterface
import com.example.mobilelab.view.registration.RegistrationActivity
import com.example.mobilelab.view.taskList.TaskListActivity

class LoginPresenter(
    private var loginView: LoginInterface?
) {

    private val context = loginView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )

    init {
        val token = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_user_token)
        )
        val nullToken = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_null_token)
        )

        if(token != nullToken) {
            loginView?.startActivity(
                Intent(context, TaskListActivity::class.java)
            )
        }
    }

    fun onDestroy() {
        loginView = null
    }

    fun onLoginButtonClick(
        email: String,
        password: String
    ) {
        if(!loginFormDataIsCorrect(
                email,
                password
            )
        ) {
            Toast
                .makeText(
                    context,
                    context.getText(R.string.login_toast_on_incorrect_login_form_data),
                    Toast.LENGTH_LONG
                ).show()

            return
        }

        Repository.loginUser(
            UserLoginForm(email, password),
            {
                Toast
                    .makeText(
                        context,
                        R.string.login_toast_on_failed,
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        ) { response ->
            if (response.body() != null) {
                sharedPreferencesHandler.saveString(
                    context.getString(R.string.shared_preferences_user_token),
                    response.body()!!.api_token
                )

                loginView?.startActivity(
                    Intent(context, TaskListActivity::class.java)
                )
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
    }

    fun onRegistrationButtonClick() {
        loginView?.startActivity(
            Intent(context, RegistrationActivity::class.java)
        )
    }

    private fun loginFormDataIsCorrect(
        email: String,
        password: String
    ): Boolean {
        val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        val emailIsCorrect = emailRegex.matchEntire(email) != null

        return email.isNotEmpty() && password.isNotEmpty() && emailIsCorrect
    }

}