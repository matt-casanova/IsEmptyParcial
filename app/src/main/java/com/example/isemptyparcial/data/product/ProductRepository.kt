package com.example.isemptyparcial.data.product

import com.example.isemptyparcial.data.RetrofitClient

class ProductRepository {
    suspend fun obtenerProductos(): List<ProductDto> {
        return RetrofitClient.instanceProduct.getProducts().products
    }
}
