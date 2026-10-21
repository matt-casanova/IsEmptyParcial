package com.example.isemptyparcial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: Int,
    val base: String,
    val quote: String,
    val rate: Double,
    val date: String,
    val agregadoEn: Long = System.currentTimeMillis()
)
