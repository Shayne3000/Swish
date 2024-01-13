package com.senijoshua.swish.presentation.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.senijoshua.swish.R
import com.senijoshua.swish.data.Team
import com.senijoshua.swish.databinding.FragmentTeamsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsFragment : Fragment(R.layout.fragment_teams) {
    private val viewModel: TeamsViewModel by viewModels()
    private lateinit var binding: FragmentTeamsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTeamsBinding.bind(view)

        binding.teamsToolbar.setNavigationIcon(R.drawable.ic_back)
        binding.teamsToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Start collection from the state flow/listening for state updates here
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { uiState ->
                    handleUiStateUpdates(uiState)
                }
            }
        }

        // trigger request for data
        viewModel.getTeam()
    }

    private fun handleUiStateUpdates(uiState: TeamsUiState) {
        when (uiState) {
            is Loading -> {
                binding.teamsContent.progressBar.isVisible = true
            }

            is Success -> {
                setupAppBarComponents(uiState.team)

                binding.teamsContent.teamsDescription.text = getString(
                    R.string.team_content_description,
                    uiState.team.id.toString(),
                    uiState.team.name,
                    uiState.team.national.toString()
                )

                binding.teamsContent.progressBar.isVisible = false
            }

            is Error -> {
                binding.teamsContent.progressBar.isVisible = false
                getSnackBar(
                    uiState.errorMessage ?: getString(R.string.generic_error_message)
                ).show()
            }
        }
    }

    private fun setupAppBarComponents(team: Team) {
        team.logo?.let {
            Glide.with(requireContext())
                .load(team.logo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.teamLogo)
        }

        binding.teamsToolbar.title = team.name
    }

    private fun getSnackBar(errorMessage: String) = Snackbar.make(
        binding.root,
        errorMessage,
        Snackbar.LENGTH_LONG
    ).setBackgroundTint(Color.RED).setTextColor(Color.WHITE)
}
