package com.example.mobilelab.model.server.user

data class UserRegistrationForm(
    val email: String,
    val name: String,
    val password: String
)