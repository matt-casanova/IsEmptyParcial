package com.example.isemptyparcial.data.local.guardados

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoPostEntity::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    abstract fun favoritoPostDao(): FavoritoPostDao

    companion object {
        @Volatile
        private var instancia: PostDatabase? = null

        fun obtener(context: Context): PostDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "posts_products.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { instancia = it }
            }
    }
}
