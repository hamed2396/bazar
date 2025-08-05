package com.example.bazar.model.repository.comment

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Comment
import com.example.bazar.model.data.Product
import com.example.bazar.model.db.ProductDao
import com.example.bazar.model.net.ApiService
import com.google.gson.JsonObject

class CommentRepositoryImpl(private val api: ApiService) :
    CommentRepository {
    override suspend fun getComments(productId: String): List<Comment> {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }
        val data = api.getComments(jsonObject)
        if (data.success) {
            return data.comments
        }
        return listOf()
    }
}


