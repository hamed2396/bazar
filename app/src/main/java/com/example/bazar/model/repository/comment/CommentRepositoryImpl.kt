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

    override suspend fun addNewComment(
        productId: String,
        text: String,
        onSuccess: (String) -> Unit
    ) {
        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
            addProperty("text", text)
        }
        val result=api.addComments(jsonObject)
        if (result.success) {
            onSuccess(result.message)
        }else{
            onSuccess(result.message)
        }
    }
}


