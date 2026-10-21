package com.example.isemptyparcial.data.local.guardados

import com.example.isemptyparcial.data.aEntidad
import com.example.isemptyparcial.data.aProductoConPublicacionUi
import com.example.isemptyparcial.models.ProductoConPublicacionUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritosPostRepository(private val dao: FavoritoPostDao) {
    fun observarFavoritos(): Flow<List<ProductoConPublicacionUi>> =
        dao.observarFavoritosPosts().map { entidades -> entidades.map { it.aProductoConPublicacionUi() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritosPosts().map { ids -> ids.toSet() }

    suspend fun agregar(item: ProductoConPublicacionUi) = withContext(Dispatchers.IO) {
        dao.insertar(item.aEntidad())
    }

    suspend fun quitar(id: Int) = withContext(Dispatchers.IO) {
        dao.eliminarPorId(id)
    }
}
