package com.example.isemptyparcial.data.local.guardados

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoPostDao {
    @Query("SELECT * FROM favoritos_posts ORDER BY agregadoEn DESC")
    fun observarFavoritosPosts(): Flow<List<FavoritoPostEntity>>

    @Query("SELECT id FROM favoritos_posts")
    fun observarIdsFavoritosPosts(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertar(favoritoPost: FavoritoPostEntity): Long

    @Query("DELETE FROM favoritos_posts WHERE id = :id")
    fun eliminarPorId(id: Int): Int
}
