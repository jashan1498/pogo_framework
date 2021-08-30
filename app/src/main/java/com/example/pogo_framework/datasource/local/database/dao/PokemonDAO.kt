package com.example.pogo_framework.datasource.local.database.dao

import androidx.room.*
import com.example.pogo_framework.models.Pokemon

@Dao
interface PokemonDAO {
    @Query("SELECT * FROM pokemon_list")
    fun getAllPokemon(): List<Pokemon>

    @Query("SELECT * FROM pokemon_list WHERE pokemon_list.name = :name limit 1")
    fun getPokemon(name:String): Pokemon?

   @Query("SELECT * FROM pokemon_list WHERE pokemon_list.id = :pokemonId limit 1")
    fun getPokemon(pokemonId:Int): Pokemon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonList(pokemon: List<Pokemon>)

    @Delete
    fun delete(pokemon: Pokemon)

    @Update
    fun update(pokemon: Pokemon)
}