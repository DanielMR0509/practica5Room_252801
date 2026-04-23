package com.example.practicaroom_252801

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practicaroom_252801.data.DataStoreManager
import com.example.practicaroom_252801.data.PokemonDatabase
import com.example.practicaroom_252801.data.PokemonRepository
import com.example.practicaroom_252801.navigation.AppNavigation
import com.example.practicaroom_252801.ui.theme.PracticaRoom_252801Theme
import com.example.practicaroom_252801.viewModel.AuthViewModel
import com.example.practicaroom_252801.viewModel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel = AuthViewModel(DataStoreManager(this))
        val database by lazy { PokemonDatabase.getDatabase(this) }
        val repository by lazy { PokemonRepository(database.pokemonDao()) }
        val pokemonViewModel: PokemonViewModel by viewModels { PokemonViewModelFactory(repository) }

        setContent {
            PracticaRoom_252801Theme {
                AppNavigation(authViewModel, pokemonViewModel)
            }
        }
    }
}

class PokemonViewModelFactory(private val repository: PokemonRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonViewModel(repository) as T
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticaRoom_252801Theme {
        Greeting("Android")
    }
}