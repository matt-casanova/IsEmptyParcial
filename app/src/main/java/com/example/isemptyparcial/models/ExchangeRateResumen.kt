package com.example.isemptyparcial.models

data class ExchangeRateResumen(
    val base: String,
    val quote: String,
    val rate: Double,
    val date: String
)
