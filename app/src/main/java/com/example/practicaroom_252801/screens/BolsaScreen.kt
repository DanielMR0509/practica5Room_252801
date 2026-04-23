package com.example.practicaroom_252801.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicaroom_252801.screens.utilities.PokemonItem
import com.example.practicaroom_252801.viewModel.PokemonViewModel

@Composable
fun BolsaScreen(pokemonViewModel: PokemonViewModel) {

    val pokemons by pokemonViewModel.getFilteredList().collectAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            pokemonViewModel.resetFilters()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = "Bolsa de Pokemon",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(16.dp))

        // --- BUSCADOR ---
        OutlinedTextField(
            value = pokemonViewModel.searchQuery,
            onValueChange = { pokemonViewModel.onSearchQueryChange(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar Pokémon") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // --- FILTRO POR TIPO ---
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = "Tipo: ${pokemonViewModel.selectedType}",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            // --- MENU DESPLEGABLE DE TIPOS ---
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                pokemonViewModel.pokemonTypes.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            pokemonViewModel.onTypeChange(tipo)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- FILTRO POR NIVEL ---
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Nivel mínimo", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${pokemonViewModel.minLevel.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = pokemonViewModel.minLevel,
                onValueChange = { pokemonViewModel.onMinLevelChange(it) },
                valueRange = 1f..100f,
                steps = 98
            )
        }

        Spacer(Modifier.height(8.dp))
        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        // --- LISTA DE POKEMNOS ---
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(pokemons, key = { it.id }) { pokemon ->
                PokemonItem(
                    pokemon = pokemon,
                    onDelete = { pokemonViewModel.deletePokemon(pokemon) },
                    onLevelUp = { pokemonViewModel.updateLevel(pokemon) }
                )
            }
        }
    }
}