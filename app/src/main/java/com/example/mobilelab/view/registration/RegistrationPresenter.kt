package com.example.mobilelab.view.registration

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R

class RegistrationPresenter(
    private var registrationView: RegistrationInterface?
) {

    fun onDestroy() {
        registrationView = null
    }

    fun onRegistrationButtonClick(
        name: String,
        email: String,
        password: String,
        password2: String
    ) {
        if (userIsRegistered(
                name,
                email,
                password,
                password2
            )
        ) {
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