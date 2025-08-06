package com.example.bazar.ui.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.model.repository.user.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    var email by mutableStateOf("")
    var postalCode by mutableStateOf("")
    var loginTime by mutableStateOf("")
    var address by mutableStateOf("")
    fun loadUserData() = viewModelScope.launch {
        email = repository.getUserName()
        loginTime = repository.getUserLoginTime()
        repository.getUserLocation().apply {
            address=first
            postalCode=second
        }

    }
}
