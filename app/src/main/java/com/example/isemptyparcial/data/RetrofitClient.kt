package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.exchange.ExchangeRateApi
import com.example.isemptyparcial.data.product.ProductApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_EXCHANGE = "https://api.frankfurter.dev/"
    private const val BASE_URL_PRODUCT = "https://dummyjson.com/"

    private val clienteHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    val instanceExchange: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_EXCHANGE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

    val instanceProduct: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PRODUCT)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}
