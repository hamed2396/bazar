package com.example.bazar.model.repository.user

interface UserRepository {

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String

    suspend fun singIn(name: String, password: String): String
    fun singOut()
    fun loadToken()
    fun saveToken(token: String)
    fun getToken(): String
    fun saveUserName(name: String)
    fun getUserName(): String
    fun saveUserLocation(address: String, postalCode: String)
    fun getUserLocation(): Pair<String, String>
    fun saveUserLoginTime()
    fun getUserLoginTime(): String


}