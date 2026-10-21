package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.local.FavoritoEntity
import com.example.isemptyparcial.data.product.ProductDto
import com.example.isemptyparcial.models.ExchangeRateResumen
import com.example.isemptyparcial.models.ProductUi

fun FavoritoEntity.aResumen(): ExchangeRateResumen =
    ExchangeRateResumen(
        base = base,
        quote = quote,
        rate = rate,
        date = date
    )

fun ExchangeRateResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(
        id = "$base-$quote".hashCode(),
        base = base,
        quote = quote,
        rate = rate,
        date = date
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
