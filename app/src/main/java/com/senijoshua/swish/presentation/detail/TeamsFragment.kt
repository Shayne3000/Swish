package com.senijoshua.swish.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
import com.senijoshua.swish.data.Teams
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
        // TODO not process death safe
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
                    handleUiStateUpdates(uiState)
                }
            }
        }

        // trigger request for data
        viewModel.getTeam(teamId)
    }

    private fun handleUiStateUpdates(uiState: TeamsUiState) {
        when (uiState) {
            is Loading -> {
                // show progressbar and hide toolbar and content
                toggleLayoutElementVisibility(shouldShowToolbar = false)
            }

            is Success -> {
                setupAppBarComponents(uiState.team)

                binding.teamsContent.teamsDescription.text = getString(
                    R.string.team_content_description,
                    uiState.team.id.toString(),
                    uiState.team.name,
                    uiState.team.national.toString()
                )

                // hide progressbar and show toolbar and content
                toggleLayoutElementVisibility(shouldShowToolbar = true)
            }

            is Error -> {
                // show empty content
                toggleLayoutElementVisibility(shouldShowToolbar = true)
                Snackbar.make(binding.root, getString(R.string.generic_error_message), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun toggleLayoutElementVisibility(shouldShowToolbar: Boolean) {
        binding.teamsContent.progressBar.isVisible = !shouldShowToolbar
        binding.teamsAppBar.isVisible = shouldShowToolbar
        binding.teamsContent.teamsDescription.isVisible = shouldShowToolbar
    }

    private fun setupAppBarComponents(team: Teams) {
        team.logo?.let {
            Glide.with(requireContext())
                .load(team.logo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(binding.teamLogo)
        }

        binding.teamsToolbar.title = team.name
        binding.teamsToolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}
