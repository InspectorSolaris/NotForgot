package com.example.mobilelab.presenter.registration

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.user.UserRegistrationForm
import com.example.mobilelab.view.registration.RegistrationInterface
import com.example.mobilelab.view.taskList.TaskListActivity

class RegistrationPresenter(
    private var registrationView: RegistrationInterface?
) {

    private val context = registrationView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )

    fun onDestroy() {
        registrationView = null
    }

    fun onRegistrationButtonClick(
        name: String,
        email: String,
        password: String,
        password2: String
    ) {
        if(!registerFormDataIsCorrect(
                name,
                email,
                password,
                password2
            )
        ) {
            Toast
                .makeText(
                    context,
                    context.getText(R.string.registration_toast_on_incorrect_register_form_data),
                    Toast.LENGTH_LONG
                ).show()

            return
        }

        Repository.registerUser(
            UserRegistrationForm(email, name, password),
            {
                Toast
                    .makeText(
                        context,
                        R.string.registration_toast_on_failed,
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

                registrationView?.startActivity(
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

    fun onLoginButtonClick() {
        registrationView?.finish()
    }

    private fun registerFormDataIsCorrect(
        name: String,
        email: String,
        password: String,
        password2: String
    ): Boolean {
        val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        val emailIsCorrect = emailRegex.matchEntire(email) != null

        return password.isNotEmpty() &&
                password == password2 &&
                name.isNotEmpty() &&
                email.isNotEmpty() &&
                emailIsCorrect
    }

}