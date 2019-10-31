package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.database.AppDatabase
import com.example.mobilelab.model.server.NotForgotAPI
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.model.server.user.UserRegistrationForm
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    private val context: Context
) {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var appDatabase: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    companion object {
        private const val APP_DATABASE_NAME = "appDatabase"
        private const val API_BASE_URL = "http://practice.mobile.kreosoft.ru/api/"
        private const val ACCEPT = "application/json"
        private const val BEARER = "Bearer"
    }

    fun loginUser(
        userLoginForm: UserLoginForm,
        onFailure: (Call<Token>, Throwable) -> Unit,
        onResponse: (Call<Token>, Response<Token>) -> Unit
    ) {
        val notForgotAPI = retrofit.create(NotForgotAPI::class.java)

        val request = notForgotAPI.postLogin(userLoginForm)

        request.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                onResponse(call, response)
            }
        })
    }

    fun registerUser(
        userRegistrationForm: UserRegistrationForm,
        onFailure: (Call<User>, Throwable) -> Unit,
        onResponse: (Call<User>, Response<User>) -> Unit
    ) {
        val notForgotAPI = retrofit.create(NotForgotAPI::class.java)

        val request = notForgotAPI.postRegister(userRegistrationForm)

        request.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                onResponse(call, response)
            }
        })
    }

}