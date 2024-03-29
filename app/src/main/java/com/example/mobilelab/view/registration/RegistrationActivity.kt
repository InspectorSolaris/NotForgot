package com.example.mobilelab.view.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.presenter.registration.RegistrationPresenter
import kotlinx.android.synthetic.main.content_registration.*

class RegistrationActivity :
    AppCompatActivity(),
    RegistrationInterface {

    private lateinit var registrationPresenter: RegistrationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        registrationPresenter = RegistrationPresenter(this)

        registrationButton.setOnClickListener {
            registrationPresenter.onRegistrationButtonClick(
                userName.text.toString(),
                userEmail.text.toString(),
                userPassword.text.toString(),
                userPassword2.text.toString()
            )
        }

        loginButton.setOnClickListener {
            registrationPresenter.onLoginButtonClick()
        }
    }

    override fun onDestroy() {
        registrationPresenter.onDestroy()

        super.onDestroy()
    }

    override fun startActivity(
        intent: Intent?
    ) {
        super.startActivity(intent)
    }

    override fun finish() {
        super.finish()
    }

}
