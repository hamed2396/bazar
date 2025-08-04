package com.example.bazar.ui.features.main

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

class MainViewModel(private val repository: ProductRepository, connected: Boolean) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(300)
            refreshData(connected)
        }
    }

    var products by mutableStateOf(listOf<Product>())
    var ads by mutableStateOf(listOf<Ads>())
    var showProgressBar by mutableStateOf(false)
    fun refreshData(connected: Boolean) = viewModelScope.launch {
        if (connected) showProgressBar = true
        val products = async { repository.getAllProducts(connected) }
        val ads = async { repository.getAds(connected) }
        updateData(products.await(), ads.await())
        showProgressBar = false


    }

   private fun updateData(product: List<Product>, ads: List<Ads>) {
        this.products=product
        this.ads=ads

    }


}