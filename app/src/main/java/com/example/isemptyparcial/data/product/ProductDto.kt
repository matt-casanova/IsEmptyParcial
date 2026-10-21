package com.example.isemptyparcial.data.product

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("products") val products: List<ProductDto> = emptyList()
)

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: Double,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("images") val images: List<String> = emptyList()
)
