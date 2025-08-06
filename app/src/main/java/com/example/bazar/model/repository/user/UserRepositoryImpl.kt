package com.example.bazar.model.repository.user

import android.content.SharedPreferences
import android.util.Log
import com.example.bazar.model.net.ApiService
import com.example.bazar.model.repository.TokenInMemory
import com.example.bazar.util.Constants.VALUE_SUCCESS
import com.google.gson.JsonObject
import androidx.core.content.edit

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
            saveUserLoginTime()
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
        sharedPrefs.edit { clear() }
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(), getToken())
    }

    override fun saveToken(token: String) {
        sharedPrefs.edit { putString("token", token) }
    }

    override fun getToken(): String = sharedPrefs.getString("token", "")!!


    override fun saveUserName(name: String) {
        sharedPrefs.edit { putString("username", name) }
    }

    override fun getUserName(): String = sharedPrefs.getString("username", "")!!

    override fun saveUserLocation(address: String, postalCode: String) {
        sharedPrefs.edit {
            putString("address", address)
            putString("postalCode", postalCode)
        }


    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sharedPrefs.getString("address", "click to add")!!
        val postalCode = sharedPrefs.getString("postalCode", "click to add")!!
        return Pair(address, postalCode)
    }

    override fun saveUserLoginTime() {
        val now = System.currentTimeMillis()
        Log.e("myTagg", "$now dd", )
        sharedPrefs.edit { putString("login_time", now.toString()) }
    }

    override fun getUserLoginTime(): String {
        return sharedPrefs.getString("login_time", "")!!
    }
}