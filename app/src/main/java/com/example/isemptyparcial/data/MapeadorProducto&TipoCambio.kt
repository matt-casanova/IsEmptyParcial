package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.local.favoritos.FavoritoEntity
import com.example.isemptyparcial.data.product.ProductDto
import com.example.isemptyparcial.models.ProductUi

fun FavoritoEntity.aProductUi(): ProductUi =
    ProductUi(
        id = id,
        title = title,
        description = description,
        category = category,
        priceUsd = priceUsd,
        priceBob = priceBob,
        priceEur = priceEur,
        thumbnail = thumbnail
    )

fun ProductUi.aEntidad(): FavoritoEntity =
    FavoritoEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        priceUsd = priceUsd,
        priceBob = priceBob,
        priceEur = priceEur,
        thumbnail = thumbnail
    )

fun ProductDto.aProductUi(rateBob: Double?, rateEur: Double?): ProductUi =
    ProductUi(
        id = id,
        title = title,
        description = description,
        category = category,
        priceUsd = price,
        priceBob = rateBob?.let { price * it },
        priceEur = rateEur?.let { price * it },
        thumbnail = thumbnail
    )
