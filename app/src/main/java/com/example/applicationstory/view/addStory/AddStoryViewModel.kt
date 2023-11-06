package com.example.applicationstory.view.addStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstory.data.response.AddStoryResponse
import com.example.applicationstory.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AddStoryViewModel : ViewModel() {

    fun uploadImage(
        imageFile: File,
        description: String,
        token: String?,
        lat: Float?,
        lon: Float?,
        onSuccess: (AddStoryResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val latRequestBody = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
                val lonRequestBody = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )

                val apiService = ApiConfig.apiInstant
                val successResponse = apiService.AddStory(multipartBody, requestBody, latRequestBody, lonRequestBody, "Bearer $token")

                withContext(Dispatchers.Main) {
                    onSuccess(successResponse)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)

                withContext(Dispatchers.Main) {
                    onError(errorResponse.message)
                }
            }
        }
    }

}