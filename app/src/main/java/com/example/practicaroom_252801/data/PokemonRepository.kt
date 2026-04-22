package com.example.practicaroom_252801.data

class PokemonRepository(private val pokemonDao: PokemonDao) {
    val allPokemons = pokemonDao.getAll()

    suspend fun add(pokemon: PokemonEntity) {
        pokemonDao.add(pokemon)
    }
}