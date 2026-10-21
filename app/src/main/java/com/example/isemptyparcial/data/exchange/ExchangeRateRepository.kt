package com.example.isemptyparcial.data.exchange

import com.example.isemptyparcial.data.RetrofitClient

class ExchangeRateRepository {
    suspend fun obtenerTiposDeCambio(): List<ExchangeRateDto> {
        return RetrofitClient.instanceExchange.getExchangeRates()
    }
}
