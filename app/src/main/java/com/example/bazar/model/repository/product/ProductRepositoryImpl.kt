package com.example.bazar.model.repository.product

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product
import com.example.bazar.model.db.ProductDao
import com.example.bazar.model.net.ApiService

class ProductRepositoryImpl(private val api: ApiService, private val dao: ProductDao) :
    ProductRepository {
    override suspend fun getAllProducts(isInternetConnected: Boolean): List<Product> {
        if (isInternetConnected) {
            val data = api.getAllProducts()
            if (data.success) {
                dao.insertOrUpdate(data.products)
                return data.products
            }


        } else {
            return dao.getAllProducts()
        }
        return listOf()
    }

    override suspend fun getAllProductsByCategory(category: String) = dao.getAllByCategory(category)

    override suspend fun getAds(isInternetConnected: Boolean): List<Ads> {
        if (isInternetConnected) {
            val data = api.getAds()
            if (data.success) {
                return data.ads
            }

        }
        return listOf()
    }

    override suspend fun getProductById(productId: String): Product {
        return dao.getProductById(productId)
    }
}
