package com.example.bazar.ui.features.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bazar.model.repository.user.UserRepository

class SignUpViewModel(private val repositoryImpl: UserRepository) : ViewModel() {

    val name= MutableLiveData("")
    val email= MutableLiveData("")
    val password= MutableLiveData("")
    val confirmPassword= MutableLiveData("")
    fun signUpUser(){

    }
}