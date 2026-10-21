package com.example.isemptyparcial.data.exchange

import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("v2/rates?base=USD&quotes=EUR,BOB")
    suspend fun getExchangeRates(): List<ExchangeRateDto>
}
