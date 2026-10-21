package com.example.isemptyparcial.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoEntity::class], version = 2, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {
    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var instancia: ExchangeDatabase? = null

        fun obtener(context: Context): ExchangeDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    ExchangeDatabase::class.java,
                    "exchange.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { instancia = it }
            }
    }
}
