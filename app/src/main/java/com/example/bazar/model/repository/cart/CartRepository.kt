package com.example.bazar.model.repository.cart

import com.example.bazar.model.data.Comment

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean
    suspend fun getCartSize():Int




}