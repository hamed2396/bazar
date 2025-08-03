package com.example.bazar.model.repository.user

import android.content.SharedPreferences
import com.example.bazar.model.net.ApiService
import com.example.bazar.model.repository.TokenInMemory
import com.example.bazar.util.Constants.VALUE_SUCCESS
import com.google.gson.JsonObject

class UserRepositoryImpl(private val api: ApiService, private val sharedPrefs: SharedPreferences) :
    UserRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String {
        val json = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", email)
            addProperty("password", password)
            addProperty("confirmPassword", confirmPassword)
        }
        val result = api.signUp(json)
        return if (result.success) {
            TokenInMemory.refreshToken(username = name, token = result.token)
            saveToken(result.token)
            saveUserName(name)
            VALUE_SUCCESS

        } else {
            result.message
        }
    }

    override suspend fun singIn(name: String, password: String): String {
        val json = JsonObject().apply {
            addProperty("name", name)
            addProperty("password", password)

        }
        val result = api.signIn(json)
        return if (result.success) {
            saveToken(result.token)
            saveUserName(name)
            VALUE_SUCCESS
        } else {
            result.message
        }
    }

    override fun singOut() {
        TokenInMemory.refreshToken(null, null)
        sharedPrefs.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(), getToken())
    }

    override fun saveToken(token: String) {
        sharedPrefs.edit().putString("token", token).apply()
    }

    override fun getToken(): String = sharedPrefs.getString("token", "")!!


    override fun saveUserName(name: String) {
        sharedPrefs.edit().putString("username", name).apply()
    }

    override fun getUserName(): String = sharedPrefs.getString("username", "")!!
}