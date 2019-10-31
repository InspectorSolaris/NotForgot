package com.example.mobilelab.presenter.registration

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.view.registration.RegistrationInterface

class RegistrationPresenter(
    private var registrationView: RegistrationInterface?
) {

    private val context = registrationView as Context
    private lateinit var sharedPreferencesHandler: SharedPreferencesHandler

    fun onDestroy() {
        registrationView = null
    }

    fun onRegistrationButtonClick(
        name: String,
        email: String,
        password: String,
        password2: String
    ) {
        sharedPreferencesHandler = SharedPreferencesHandler(
            context,
            context.getString(R.string.shared_preferences_file)
        )

        if (userIsRegistered(
                name,
                email,
                password,
                password2
            )
        ) {
            sharedPreferencesHandler.saveString(
                context.getString(R.string.shared_preferences_user_key),
                email
            )
            registrationView?.onSuccessRegistration()
        } else {
            Toast
                .makeText(
                    registrationView as Context,
                    R.string.registration_toast_unable_to_login,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    fun onLoginButtonClick() {
        registrationView?.onLoginButtonClick()
    }

    private fun userIsRegistered(
        name: String,
        email: String,
        password: String,
        password2: String
    ): Boolean {
        return name.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                password2.isNotEmpty()
    }

}