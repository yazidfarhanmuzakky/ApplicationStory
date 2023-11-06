package com.example.applicationstory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstory.data.UserRepository
import com.example.applicationstory.data.pref.UserModel
import com.example.applicationstory.data.response.LoginResponse
import com.example.applicationstory.data.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService
    ) : ViewModel() {

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
    }


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}