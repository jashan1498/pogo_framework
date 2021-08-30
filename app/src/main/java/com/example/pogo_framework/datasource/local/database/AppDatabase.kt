package com.example.pogo_framework.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.SkipQueryVerification
import com.example.pogo_framework.datasource.local.database.dao.PokemonDAO
import com.example.pogo_framework.datasource.local.database.dao.TeamDAO
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.models.Pokemon

@Database(entities = [Pokemon::class, CapturedModel::class], version = 1, exportSchema = false)
@SkipQueryVerification
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
    abstract fun pokemonTeamTao(): TeamDAO

    companion object {
        var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pogo_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return appDatabase!!
        }
    }
}