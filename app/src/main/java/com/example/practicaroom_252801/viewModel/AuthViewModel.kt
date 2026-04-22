package com.example.practicaroom_252801.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaroom_252801.data.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    val isLoggedIn = dataStore.isLoggedInFlow.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = false
    )

    val username = dataStore.usernameFlow.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ""
    )

    fun login(user: String, pass: String) {
        if (user == "admin" && pass == "1234") {
            viewModelScope.launch {
                dataStore.saveSession(username = user)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.logout()
        }
    }
}