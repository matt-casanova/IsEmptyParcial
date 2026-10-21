package com.example.isemptyparcial.models

data class ProductUi(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val priceUsd: Double,
    val priceBob: Double?,
    val priceEur: Double?,
    val thumbnail: String?
)
