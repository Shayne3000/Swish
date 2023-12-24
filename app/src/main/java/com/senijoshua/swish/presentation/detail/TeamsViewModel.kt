package com.senijoshua.swish.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.data.Teams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    // Setup a ui state holder using StateFlow
    private val _uiState = MutableStateFlow(Loading)
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    // Start a coroutine (in the viewModelScope) within which you send a network request
    fun getTeam(id: Int) {
        viewModelScope.launch {

        }
    }
}

/**
 * Interface that represents the UI state of the fragment
 */
sealed interface TeamsUiState
data class Success(val team: Teams) : TeamsUiState
data class Error(val errorMessage: String) : TeamsUiState
data object Loading : TeamsUiState

