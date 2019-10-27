package com.example.mobilelab.model.server.user

data class User(
    val email: String,
    val name: String,
    val id: Int,
    val api_token: Int
)