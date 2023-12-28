package com.senijoshua.swish.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.data.Result
import com.senijoshua.swish.data.Team
import com.senijoshua.swish.util.TEAM_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val repository: MainRepository,
    savedState: SavedStateHandle // savedState has access to the arguments bundle sent to Fragment and it stores UI state across process death.
) : ViewModel() {
    // Setup a ui state holder using StateFlow
    private val _uiState = MutableStateFlow<TeamsUiState>(Loading)
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    private val teamId: Int = savedState[TEAM_ID]!!

    // Start a coroutine (in the viewModelScope) within which you send a network request
    fun getTeam() {
        viewModelScope.launch {
            when (val result = repository.getTeam(teamId)) {
                is Result.Loading -> {
                    _uiState.value = Loading
                }

                is Result.Success -> {
                    _uiState.value = Success(result.data)
                }

                is Result.Error -> {
                    _uiState.value = Error(result.error.message)
                }
            }
        }
    }
}

/**
 * Interface that represents the UI state of the fragment
 */
sealed interface TeamsUiState
data class Success(val team: Team) : TeamsUiState
data class Error(val errorMessage: String?) : TeamsUiState
data object Loading : TeamsUiState

