package com.example.bazar.ui.features.cart

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazar.model.data.Product
import com.example.bazar.model.repository.cart.CartRepository
import com.example.bazar.model.repository.user.UserRepository
import com.example.bazar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val productList = mutableStateOf(listOf<Product>())
    val totalPrice = mutableIntStateOf(0)
    val isChangingNumber = mutableStateOf(Pair("", false))


    fun purchaseAll(address: String, postalCode: String, IsSuccess: (Boolean, String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = cartRepository.submitOrder(address, postalCode)
            IsSuccess.invoke(result.success, result.paymentLink)

        }


    }

    fun setPaymentStatus(status :Int) {
        cartRepository.setPurchaseStatus(status)
    }


    fun getUserLocation(): Pair<String, String> {
        return userRepository.getUserLocation()
    }

    fun setUserLocation(address: String, postalCode: String) {
        userRepository.saveUserLocation(address, postalCode)
    }

    fun loadCartData() {

        viewModelScope.launch(coroutineExceptionHandler) {

            val data = cartRepository.getUserCartInfo()
            productList.value = data.productList
            totalPrice.value = data.totalPrice

        }


    }

    fun addItem(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productId, true)

            val isSuccess = cartRepository.addToCart(productId)
            if (isSuccess) {
                loadCartData()
            }

            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)

        }

    }

    fun removeItem(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productId, true)

            val isSuccess = cartRepository.removeFromCart(productId)
            if (isSuccess) {
                loadCartData()
            }

            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)

        }

    }


}