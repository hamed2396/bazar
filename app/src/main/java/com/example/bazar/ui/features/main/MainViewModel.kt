package com.example.bazar.ui.features.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.model.repository.user.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProductRepository, connected: Boolean) : ViewModel() {
    init {
        refreshData(connected)
    }

    val products = mutableStateOf(listOf<Product>())
    val ads = mutableStateOf(listOf<Ads>())
    val showProgressBar = mutableStateOf(false)
    fun refreshData(connected: Boolean) = viewModelScope.launch {
        if (connected) showProgressBar.value = true
        val products = async { repository.getAllProducts(connected) }
        val ads = async { repository.getAds(connected) }
        updateData(products.await(), ads.await())
        showProgressBar.value = false


    }

   private fun updateData(product: List<Product>, ads: List<Ads>) {
        this.products.value=product
        this.ads.value=ads

    }


}