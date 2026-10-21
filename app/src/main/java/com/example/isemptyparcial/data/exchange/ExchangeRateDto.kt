package com.example.isemptyparcial.data.exchange

import com.google.gson.annotations.SerializedName

data class ExchangeRateDto(
    @SerializedName("date") val date: String? = null,
    @SerializedName("base") val base: String? = null,
    @SerializedName("quote") val quote: String? = null,
    @SerializedName("rate") val rate: Double? = null
)
