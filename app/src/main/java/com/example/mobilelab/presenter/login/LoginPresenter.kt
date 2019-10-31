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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginPresenter(
    private var loginView: LoginInterface?,
    private val applicationContext: Context
) {

    private val context = loginView as Context
    private lateinit var repository: Repository
    private lateinit var sharedPreferencesHandler: SharedPreferencesHandler

    fun onDestroy() {
        loginView = null
    }

    fun onLoginButtonClick(
        email: String,
        password: String
    ) {
        repository = Repository(applicationContext)
        sharedPreferencesHandler = SharedPreferencesHandler(
            context,
            context.getString(R.string.shared_preferences_file)
        )

        repository.loginUser(
            UserLoginForm(email, password),
            { call: Call<Token>, throwable: Throwable ->
                Toast
                    .makeText(
                        context,
                        R.string.login_toast_unable_to_login,
                        Toast.LENGTH_LONG
                    )
                    .show()
            },
            { call: Call<Token>, response: Response<Token> ->
                if(response.body() != null)
                {
                    sharedPreferencesHandler.saveString(
                        context.getString(R.string.shared_preferences_token),
                        response.body()!!.api_token
                    )

                    loginView?.onSuccessLogin()
                }
            }
        )
    }

    fun onRegistrationButtonClick() {
        loginView?.onRegistrationButtonClick()
    }

}