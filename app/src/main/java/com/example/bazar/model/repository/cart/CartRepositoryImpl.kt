package com.example.bazar.model.repository.cart

import com.example.bazar.model.data.Comment
import com.example.bazar.model.net.ApiService
import com.google.gson.JsonObject

class CartRepositoryImpl(private val api: ApiService) : CartRepository {


    override suspend fun addToCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val result = api.addToCart(jsonObject)
        return result.success
    }

    override suspend fun getCartSize(): Int {
        val result = api.getUserCart()
        var counter = 0
        if (result.success) {
            result.productList.forEach {
                counter += (it.quantity ?: "0").toInt()
            }
            return counter
        }
        return 0
    }
}


