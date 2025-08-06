package com.example.bazar.ui.features.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.model.repository.user.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: ProductRepository) : ViewModel() {

    var products by mutableStateOf(listOf<Product>())
    fun loadCategoryData(category: String) = viewModelScope.launch {

        val localData = repository.getAllProductsByCategory(category)
        products = localData


    }

}
