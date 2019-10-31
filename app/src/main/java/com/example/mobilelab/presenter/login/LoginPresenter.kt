package com.example.mobilelab.presenter.login

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.view.login.LoginInterface
import retrofit2.Call
import retrofit2.Response

class LoginPresenter(
    private var loginView: LoginInterface?,
    applicationContext: Context
) {

    private val context = loginView as Context
    private val repository = Repository(applicationContext)
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
            { call: Call<Token>, throwable: Throwable ->
                Toast
                    .makeText(
                        context,
                        R.string.login_toast_on_failed,
                        Toast.LENGTH_LONG
                    )
                    .show()
            },
            { call: Call<Token>, response: Response<Token> ->
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