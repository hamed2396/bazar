package com.example.bazar.model.repository

object TokenInMemory {
    var userName: String? = null
        private set
    var token: String? = null
        private set

    fun refreshToken(username: String?, token: String?) {
        this.userName = username
        this.token = token
    }
}