package com.example.mobilelab.presenter.registration

import android.content.Context
import android.widget.Toast
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserRegistrationForm
import com.example.mobilelab.view.registration.RegistrationInterface
import retrofit2.Call
import retrofit2.Response

class RegistrationPresenter(
    private var registrationView: RegistrationInterface?,
    private val applicationContext: Context
) {

    private val context = registrationView as Context
    private lateinit var repository: Repository
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
        if(!registerFormDataIsCorrect(
                name,
                email,
                password,
                password2
            ))
        {
            Toast
                .makeText(
                    context,
                    context.getText(R.string.registration_toast_incorrect_register_form_data),
                    Toast.LENGTH_LONG
                ).show()

            return
        }

        repository = Repository(applicationContext)
        sharedPreferencesHandler = SharedPreferencesHandler(
            context,
            context.getString(R.string.shared_preferences_file)
        )

        repository.registerUser(
            UserRegistrationForm(email, name, password),
            { call: Call<User>, throwable: Throwable ->
                Toast
                    .makeText(
                        context,
                        R.string.registration_toast_unable_to_register,
                        Toast.LENGTH_LONG
                    )
                    .show()
            },
            { call: Call<User>, response: Response<User> ->
                if (response.body() != null) {
                    sharedPreferencesHandler.saveString(
                        context.getString(R.string.shared_preferences_token),
                        response.body()!!.api_token
                    )

                    registrationView?.onSuccessRegistration()
                }
            }
        )
    }

    fun onLoginButtonClick() {
        registrationView?.onLoginButtonClick()
    }

    private fun registerFormDataIsCorrect(
        name: String,
        email: String,
        password: String,
        password2: String
    ): Boolean {
        return password.isNotEmpty() && password == password2 && name.isNotEmpty() && email.isNotEmpty()
    }

}