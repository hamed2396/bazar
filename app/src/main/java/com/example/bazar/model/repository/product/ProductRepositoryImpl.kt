package com.example.bazar.model.repository.product

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product
import com.example.bazar.model.db.ProductDao
import com.example.bazar.model.net.ApiService

class ProductRepositoryImpl(private val api: ApiService,private val dao: ProductDao) : ProductRepository{
    override suspend fun getAllProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAds(): List<Ads> {
        TODO("Not yet implemented")
    }
}
