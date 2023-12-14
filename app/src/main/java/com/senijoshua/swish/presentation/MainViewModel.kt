package com.senijoshua.swish.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.data.Result
import com.senijoshua.swish.data.Teams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {
    // Add a state holder using StateFlow
    // Stop Activity recreation and Handle configuration change in the activity.
    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState

    // Start network request with a coroutine in the viewmodel scope.
    fun getTeams() {
        viewModelScope.launch {
            when (val result = repo.loadTeams()) {
                is Result.Success -> {
                    _uiState.value = MainState.Success(result.data)
                }

                is Result.Error -> {
                    _uiState.value = MainState.Error(result.error.message)
                }
            }
        }
    }
}

// Add a state representation with a sealed class
sealed class MainState {
    data class Success(val data: List<Teams>) : MainState()
    data class Error(val errorMessage: String?) : MainState()
    data object Loading : MainState()
}
