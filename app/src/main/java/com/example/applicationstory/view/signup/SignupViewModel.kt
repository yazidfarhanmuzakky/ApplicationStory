package com.example.applicationstory.view.signup

import androidx.lifecycle.ViewModel
import com.example.applicationstory.data.response.RegisterResponse
import com.example.applicationstory.data.retrofit.ApiService
import retrofit2.Call

class SignupViewModel(
    private val apiService: ApiService
) : ViewModel() {

    fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        return apiService.register(name, email, password)
    }

}