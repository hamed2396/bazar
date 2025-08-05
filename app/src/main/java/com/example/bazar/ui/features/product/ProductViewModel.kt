package com.example.bazar.ui.features.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    var product by mutableStateOf(
        Product(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )

    private fun loadProductFromCache(productId: String) = viewModelScope.launch(coroutineExceptionHandler) {
        product = repository.getProductById(productId)
    }

     fun loadData(productId: String) = viewModelScope.launch {
        loadProductFromCache(productId)
    }

}

