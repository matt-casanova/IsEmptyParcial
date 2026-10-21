package com.example.isemptyparcial.data.local.favoritos

import com.example.isemptyparcial.data.aEntidad
import com.example.isemptyparcial.data.aProductUi
import com.example.isemptyparcial.models.ProductUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritosRepository(private val dao: FavoritoDao) {
    fun observarFavoritos(): Flow<List<ProductUi>> =
        dao.observarFavoritos().map { entidades -> entidades.map { it.aProductUi() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(producto: ProductUi) = withContext(Dispatchers.IO) {
        dao.insertar(producto.aEntidad())
    }

    suspend fun quitar(id: Int) = withContext(Dispatchers.IO) {
        dao.eliminarPorId(id)
    }
}
