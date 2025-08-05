package com.example.bazar.model.repository.comment

import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Comment
import com.example.bazar.model.data.Product

interface CommentRepository {

    suspend fun getComments(productId: String): List<Comment>
    suspend fun addNewComment(productId: String,text: String,onSuccess:(String)-> Unit)


}