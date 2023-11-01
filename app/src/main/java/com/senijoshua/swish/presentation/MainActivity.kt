package com.senijoshua.swish.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.senijoshua.swish.R
import com.senijoshua.swish.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inflate views
        binding.toolbar.title = "NBA List"
        binding.toolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(binding.toolbar)

        // Setup list adapter
        adapter = MainAdapter(this)
        binding.nbaList.adapter = adapter
        binding.nbaList.layoutManager = LinearLayoutManager(this)
        binding.nbaList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // collect and start listening to ViewModel's StateFlow
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    handleUiState(uiState)
                }
            }
        }

        vm.getTeams()
    }

    private fun handleUiState(uiState: MainState) {
        when (uiState) {
            is MainState.Loading -> {
                binding.progressBar.isVisible = true
            }

            is MainState.Success -> {
                binding.progressBar.isVisible = false
                binding.nbaList.isVisible = true
                adapter.submitList(uiState.data)
            }

            is MainState.Error -> {
                binding.progressBar.isVisible = false
                val errorMessage = uiState.errorMessage ?: getString(R.string.generic_error_message)
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
