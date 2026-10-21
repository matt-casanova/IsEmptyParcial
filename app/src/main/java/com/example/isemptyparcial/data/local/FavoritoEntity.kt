package com.example.isemptyparcial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val priceUsd: Double,
    val priceBob: Double?,
    val priceEur: Double?,
    val thumbnail: String?,
    val agregadoEn: Long = System.currentTimeMillis()
)
