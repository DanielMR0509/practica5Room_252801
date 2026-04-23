package com.example.practicaroom_252801.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_table ORDER BY number ASC")
    fun getAll(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(pokemon: PokemonEntity)

    @Delete
    suspend fun delete(pokemon: PokemonEntity)

    @Update
    suspend fun update(pokemon: PokemonEntity)

    @Query("""
        SELECT * FROM pokemon_table 
        WHERE (name LIKE :searchQuery OR type LIKE :searchQuery)
        AND level >= :minLevel
        AND (:typeFilter = 'Todos' OR type LIKE '%' || :typeFilter || '%')
    """)
    fun getFilteredPokemons(
        searchQuery: String,
        minLevel: Int,
        typeFilter: String
    ): Flow<List<PokemonEntity>>
}