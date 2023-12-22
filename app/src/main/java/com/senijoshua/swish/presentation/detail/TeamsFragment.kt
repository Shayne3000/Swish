package com.senijoshua.swish.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.senijoshua.swish.R
import com.senijoshua.swish.databinding.FragmentTeamsBinding
import com.senijoshua.swish.util.TEAM_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamsFragment : Fragment(R.layout.fragment_teams) {
    private val viewModel: TeamsViewModel by viewModels()
    private lateinit var binding: FragmentTeamsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTeamsBinding.bind(view)
        val teamId = requireArguments().getInt(TEAM_ID)

        // UI setup here
        binding.teamsToolbar

        // initialise collectiong from the state flow here

        // trigger request for data
    }

    companion object {
        fun newInstance() = TeamsFragment()
    }
}
