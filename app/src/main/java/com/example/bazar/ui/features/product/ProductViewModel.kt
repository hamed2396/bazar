package com.example.bazar.ui.features.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Comment
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.cart.CartRepository
import com.example.bazar.model.repository.comment.CommentRepository
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.listOf

class ProductViewModel(
    private val repository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
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
    var comments by mutableStateOf(listOf<Comment>())
    var isAddingProduct by mutableStateOf(false)

    private fun loadProductFromCache(productId: String) =
        viewModelScope.launch(coroutineExceptionHandler) {
            product = repository.getProductById(productId)
        }

    private fun loadAllComments(productId: String) =
        viewModelScope.launch(coroutineExceptionHandler) {
            comments = commentRepository.getComments(productId)
        }

    fun addNewComment(productId: String, text: String, onSuccess: (String) -> Unit) =
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, text, onSuccess)
            delay(100)
            comments = commentRepository.getComments(productId)
        }

    fun addToCart(productId: String,addingResult:(String)-> Unit) = viewModelScope.launch(coroutineExceptionHandler) {
        isAddingProduct=true
        val result = cartRepository.addToCart(productId)
        delay(500)
        isAddingProduct=false
        if (result) {
            addingResult("product added")
        }else{
            addingResult("product Not Added")
        }

    }

    fun loadData(productId: String, isConnected: Boolean) = viewModelScope.launch {
        loadProductFromCache(productId)
        if (isConnected) {
            loadAllComments(productId)
        }
    }


}

