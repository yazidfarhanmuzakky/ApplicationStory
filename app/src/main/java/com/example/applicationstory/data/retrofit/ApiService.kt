package com.example.applicationstory.data.retrofit

import com.example.applicationstory.data.response.StoryResponseItem
import com.example.applicationstory.data.response.AddStoryResponse
import com.example.applicationstory.data.response.DetailStoryResponse
import com.example.applicationstory.data.response.LoginResponse
import com.example.applicationstory.data.response.QuoteResponse
import com.example.applicationstory.data.response.RegisterResponse
import com.example.applicationstory.data.response.StoriesResponse
import com.example.applicationstory.data.response.StoriesResponseList
import com.example.applicationstory.data.response.StoryItem
import com.example.applicationstory.data.response.StoryPagingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") authorization: String
    ): QuoteResponse

    @GET("stories/{id}")
    fun detailStories(
        @Path("id") username: String,
        @Header("Authorization") authorization: String,
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun AddStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") authorization: String,
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int,
        @Header("Authorization") authorization: String,
    ): Response<StoriesResponse>
}