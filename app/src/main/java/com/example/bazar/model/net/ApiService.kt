package com.example.bazar.model.net

import com.example.bazar.model.data.LoginResponse
import com.example.bazar.model.repository.TokenInMemory
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("signUp")
    suspend fun signUp(@Body jsonObject: JsonObject): LoginResponse

    @POST("signIn")
    suspend fun signIn(@Body jsonObject: JsonObject): LoginResponse

    @GET("refreshToken")
    fun refreshToken(): Call<LoginResponse>
}

fun createApiService(): ApiService {
    val okHttpClient = OkHttpClient().newBuilder().addInterceptor {
        val oldRequest = it.request()
        val newRequest = oldRequest.newBuilder()
        if (TokenInMemory.token != null) {
            newRequest.addHeader("Authorization", TokenInMemory.token!!)
            newRequest.method(oldRequest.method,)
        }.build()
    }
    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiService::class.java)
}