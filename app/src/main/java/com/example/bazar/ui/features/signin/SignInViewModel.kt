package com.example.bazar.ui.features.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bazar.model.repository.user.UserRepository
import com.example.bazar.model.repository.user.UserRepositoryImpl

class SignInViewModel(private val repository: UserRepository) : ViewModel() {


    val email= MutableLiveData("")
    val password= MutableLiveData("")

    fun signInUser(){

    }
}