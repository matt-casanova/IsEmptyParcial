package com.example.isemptyparcial.data.post

import com.example.isemptyparcial.data.RetrofitClient

class PostRepository {
    suspend fun obtenerPublicaciones(): List<PostDto> {
        return RetrofitClient.instancePost.getPosts()
    }
}
