package com.example.bazar.ui.features.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.repository.user.UserRepository
import com.example.bazar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")
    fun signUpUser(loggingEvent: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {

            val result =
                repository.signUp(name.value!!, email.value!!, password.value!!, password.value!!)
            loggingEvent(result)
        }
    }
}