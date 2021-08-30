package com.example.pogo_framework.datasource.local.database.dao

import androidx.room.*
import com.example.pogo_framework.models.CapturedModel

@Dao
interface TeamDAO {
    @Query("SELECT * FROM pokemon_team")
    fun getTeamPokemon(): List<CapturedModel>

    @Query("SELECT * FROM pokemon_team WHERE pokemon_team.name = :name limit 1")
    fun getPokemon(name: String): CapturedModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonList(pokemon: List<CapturedModel>)

    @Delete
    fun delete(pokemon: CapturedModel)

    @Update
    fun update(pokemon: CapturedModel)
}