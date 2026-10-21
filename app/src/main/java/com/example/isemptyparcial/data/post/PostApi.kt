package com.example.isemptyparcial.data.post

import retrofit2.http.GET

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>
}
