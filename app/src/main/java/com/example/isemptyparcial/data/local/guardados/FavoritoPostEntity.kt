package com.example.isemptyparcial.data.local.guardados

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos_posts")
data class FavoritoPostEntity(
    @PrimaryKey val id: Int,
    val tituloProducto: String,
    val descripcionProducto: String,
    val categoriaProducto: String,
    val precioUsd: Double,
    val imagenUrl: String?,
    val publicacionId: Int?,
    val tituloPublicacion: String?,
    val contenidoPublicacion: String?,
    val agregadoEn: Long = System.currentTimeMillis()
)
