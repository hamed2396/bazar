package com.example.bazar.model.net

import com.example.bazar.model.data.AddNewCommentResponse
import com.example.bazar.model.data.AdsResponse
import com.example.bazar.model.data.CartResponse
import com.example.bazar.model.data.CommentResponse
import com.example.bazar.model.data.LoginResponse
import com.example.bazar.model.data.ProductResponse
import com.example.bazar.model.repository.TokenInMemory
import com.example.bazar.util.Constants
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

    @GET("getProducts")
    suspend fun getAllProducts(): ProductResponse

    @GET("getSliderPics")
    suspend fun getAds(): AdsResponse

    @POST("getComments")
    suspend fun getComments(@Body jsonObject: JsonObject): CommentResponse
    @POST("addNewComment")
    suspend fun addComments(@Body jsonObject: JsonObject): AddNewCommentResponse
    @POST("addToCart")
    suspend fun addToCart(@Body jsonObject: JsonObject): CartResponse


}
fun createApiService(): ApiService {
    val okHttpClient = OkHttpClient().newBuilder().addInterceptor {
        val oldRequest = it.request()
        val newRequest = oldRequest.newBuilder()
        if (TokenInMemory.token != null) {

            newRequest.addHeader("Authorization", TokenInMemory.token!!)
        }

        newRequest.method(oldRequest.method, oldRequest.body)
        it.proceed(newRequest.build())

    }.build()

    return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiService::class.java)
}