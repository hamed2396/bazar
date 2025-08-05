package com.example.bazar.model.repository.product

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>
    suspend fun getAllProductsByCategory(category: String): List<Product>
    suspend fun getAds(isInternetConnected: Boolean): List<Ads>
    suspend fun getProductById(productId:String): Product


}