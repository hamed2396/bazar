package com.example.bazar.model.repository.product

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(): List<Product>
    suspend fun getAllAds(): List<Ads>


}