package com.example.bazar.model.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.bazar.model.data.Product

@Dao
interface ProductDao {
    @Upsert
    suspend fun insertOrUpdate(product: List<Product>)

    @Query("Select * from product_table")
    suspend fun getAllProducts(): List<Product>

    @Query("Select * from product_table where productId =:productId")
    suspend fun getProductById(productId: String): Product

    @Query("Select * from product_table where category =:category")
    suspend fun getAllByCategory(category: String): List<Product>


}