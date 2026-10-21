package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.local.guardados.FavoritoPostEntity
import com.example.isemptyparcial.data.post.PostDto
import com.example.isemptyparcial.data.product.ProductDto
import com.example.isemptyparcial.models.ProductoConPublicacionUi

fun FavoritoPostEntity.aProductoConPublicacionUi(): ProductoConPublicacionUi =
    ProductoConPublicacionUi(
        productoId = id,
        tituloProducto = tituloProducto,
        descripcionProducto = descripcionProducto,
        categoriaProducto = categoriaProducto,
        precioUsd = precioUsd,
        imagenUrl = imagenUrl,
        publicacionId = publicacionId,
        tituloPublicacion = tituloPublicacion,
        contenidoPublicacion = contenidoPublicacion
    )

fun ProductoConPublicacionUi.aEntidad(): FavoritoPostEntity =
    FavoritoPostEntity(
        id = productoId,
        tituloProducto = tituloProducto,
        descripcionProducto = descripcionProducto,
        categoriaProducto = categoriaProducto,
        precioUsd = precioUsd,
        imagenUrl = imagenUrl,
        publicacionId = publicacionId,
        tituloPublicacion = tituloPublicacion,
        contenidoPublicacion = contenidoPublicacion
    )

fun combinarProductoYPublicacion(
    product: ProductDto,
    post: PostDto?
): ProductoConPublicacionUi {
    return ProductoConPublicacionUi(
        productoId = product.id,
        tituloProducto = product.title,
        descripcionProducto = product.description,
        categoriaProducto = product.category,
        precioUsd = product.price,
        imagenUrl = product.thumbnail,
        publicacionId = post?.id,
        tituloPublicacion = post?.title,
        contenidoPublicacion = post?.body
    )
}
