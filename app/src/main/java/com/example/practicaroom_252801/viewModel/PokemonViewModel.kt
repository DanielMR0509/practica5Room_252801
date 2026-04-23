package com.example.practicaroom_252801.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaroom_252801.data.PokemonEntity
import com.example.practicaroom_252801.data.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val availablePokemons = listOf(
        PokemonEntity(name = "Gengar", number = "094", type = "Fantasma"),
        PokemonEntity(name = "Dragonite", number = "149", type = "Dragón"),
        PokemonEntity(name = "Scizor", number = "212", type = "Bicho"),
        PokemonEntity(name = "Tyranitar", number = "248", type = "Roca"),
        PokemonEntity(name = "Blaziken", number = "257", type = "Fuego"),
        PokemonEntity(name = "Gardevoir", number = "282", type = "Psíquico"),
        PokemonEntity(name = "Milotic", number = "350", type = "Agua"),
        PokemonEntity(name = "Metagross", number = "376", type = "Acero"),
        PokemonEntity(name = "Garchomp", number = "445", type = "Dragón"),
        PokemonEntity(name = "Lucario", number = "448", type = "Lucha"),
        PokemonEntity(name = "Togekiss", number = "468", type = "Hada"),
        PokemonEntity(name = "Chandelure", number = "609", type = "Fantasma"),
        PokemonEntity(name = "Haxorus", number = "612", type = "Dragón"),
        PokemonEntity(name = "Hydreigon", number = "635", type = "Siniestro"),
        PokemonEntity(name = "Aegislash", number = "681", type = "Acero"),
        PokemonEntity(name = "Mimikyu", number = "778", type = "Fantasma"),
        PokemonEntity(name = "Dragapult", number = "887", type = "Dragón"),
        PokemonEntity(name = "Toxtricity", number = "849", type = "Eléctrico"),
        PokemonEntity(name = "Urshifu", number = "892", type = "Lucha"),
        PokemonEntity(name = "Tinkaton", number = "959", type = "Hada"),
        PokemonEntity(name = "Baxcalibur", number = "998", type = "Dragón"),
        PokemonEntity(name = "Iron Valiant", number = "1006", type = "Hada"),
        PokemonEntity(name = "Koraidon", number = "1007", type = "Lucha")
    )
    val pokemonTypes = listOf("Todos", "Fuego", "Agua", "Planta", "Eléctrico", "Lucha", "Normal", "Veneno", "Psíquico", "Roca", "Fantasma", "Acero", "Hada", "Dragón")

    var wildPokemon by mutableStateOf<PokemonEntity?>(null)
        private set

    var capturedPokemons by mutableStateOf(listOf<PokemonEntity>())
        private set

    var pokemonSeEscapo by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedType by mutableStateOf("Todos")
        private set

    var minLevel by mutableFloatStateOf(1f)
        private set

    fun searchPokemon(){
        wildPokemon = availablePokemons.random()
    }

    fun releaseCapturedPokemons(){
        capturedPokemons = emptyList()
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun onTypeChange(newType: String) {
        selectedType = newType
    }

    fun onMinLevelChange(newLevel: Float) {
        minLevel = newLevel
    }

    fun capturePokemon(){
        wildPokemon?.let {
            val succes = (1..100).random()
            if(succes > 50){
                capturedPokemons = capturedPokemons + it
                pokemonSeEscapo = false
                wildPokemon = null
            } else {
                pokemonSeEscapo = true
                wildPokemon = null
            }
        }
    }

    val pokemonsState: StateFlow<List<PokemonEntity>> = repository.allPokemons
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPokemon(name: String, number: String, type: String, level: Int = 1) {
        viewModelScope.launch {
            repository.add(
                PokemonEntity(
                    name = name,
                    number = number,
                    type = type,
                    level = level
                )
            )
        }
    }

    fun deletePokemon(pokemon: PokemonEntity) {
        viewModelScope.launch { repository.delete(pokemon) }
    }

    fun updateLevel(pokemon: PokemonEntity) {
        if (pokemon.level < 100) {
            val exito = (1..100).random()
            if (exito <= 70) {
                viewModelScope.launch {
                    val pokemonActualizado = pokemon.copy(level = pokemon.level + 1)
                    repository.update(pokemonActualizado)
                }
            }
        }
    }

    fun getFilteredList(): Flow<List<PokemonEntity>> {
        return repository.getFilteredPokemons(
            query = "%$searchQuery%",
            minLevel = minLevel.toInt(),
            type = selectedType
        )
    }

    fun resetFilters() {
        onSearchQueryChange("")
        onTypeChange("Todos")
        onMinLevelChange(1f)
    }

    fun resetCaptureState() {
        wildPokemon = null
        pokemonSeEscapo = false
        releaseCapturedPokemons()
    }
}