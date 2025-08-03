package com.example.bazar.ui.features.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.repository.user.UserRepository
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: UserRepository) : ViewModel() {


    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun signInUser(onSignInEvent: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.singIn(email.value!!, password.value!!)
            onSignInEvent(result)
        }
    }
}