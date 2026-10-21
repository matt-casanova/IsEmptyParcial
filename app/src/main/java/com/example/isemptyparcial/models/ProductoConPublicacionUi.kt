package com.example.isemptyparcial.models

data class ProductoConPublicacionUi(
    val productoId: Int,
    val tituloProducto: String,
    val descripcionProducto: String,
    val categoriaProducto: String,
    val precioUsd: Double,
    val imagenUrl: String?,
    // Publicación asociada (JSONPlaceholder)
    val publicacionId: Int?,
    val tituloPublicacion: String?,
    val contenidoPublicacion: String?
)
