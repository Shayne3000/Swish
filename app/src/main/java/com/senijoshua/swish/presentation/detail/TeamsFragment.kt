package com.senijoshua.swish.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.senijoshua.swish.R
import com.senijoshua.swish.databinding.FragmentTeamsBinding
import com.senijoshua.swish.util.TEAM_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsFragment : Fragment(R.layout.fragment_teams) {
    private val viewModel: TeamsViewModel by viewModels()
    private lateinit var binding: FragmentTeamsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTeamsBinding.bind(view)
        val teamId = requireArguments().getInt(TEAM_ID)

        // Setup the UI layer, then implement the data layer
        binding.teamsToolbar.setNavigationIcon(R.drawable.ic_back)
        binding.teamsToolbar.setNavigationOnClickListener {
            requireActivity().onNavigateUp()
        }

        // Start collection from the state flow here
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect{ uiState ->
                    handleUiState(uiState)
                }
            }
        }

        // trigger request for data
        viewModel.getTeam(teamId)
    }

    private fun handleUiState(uiState: TeamsUiState) {
        when (uiState) {
            is Loading -> {

            }

            is Success -> {

            }

            is Error -> {

            }
        }
    }
}
