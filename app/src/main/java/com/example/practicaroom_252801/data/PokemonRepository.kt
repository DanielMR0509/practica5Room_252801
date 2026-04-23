package com.example.practicaroom_252801.data

class PokemonRepository(private val pokemonDao: PokemonDao) {
    val allPokemons = pokemonDao.getAll()

    suspend fun add(pokemon: PokemonEntity) {
        pokemonDao.add(pokemon)
    }

    suspend fun delete(pokemon: PokemonEntity){
        pokemonDao.delete(pokemon)
    }

    suspend fun update(pokemon: PokemonEntity) {
        pokemonDao.update(pokemon)
    }

    fun getFilteredPokemons(query: String, minLevel: Int, type: String) =
        pokemonDao.getFilteredPokemons("%$query%", minLevel, type)
}