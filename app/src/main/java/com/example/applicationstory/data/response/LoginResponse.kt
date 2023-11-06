package com.example.applicationstory.data.response

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult?
) {
    data class LoginResult (
        val userId: String,
        val name: String,
        val token: String
    )
}