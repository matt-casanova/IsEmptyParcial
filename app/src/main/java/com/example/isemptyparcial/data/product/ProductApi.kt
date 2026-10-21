package com.example.isemptyparcial.data.product

import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): ProductResponseDto
}
