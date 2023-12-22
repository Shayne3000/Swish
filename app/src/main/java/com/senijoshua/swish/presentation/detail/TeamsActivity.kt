package com.senijoshua.swish.presentation.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.senijoshua.swish.R
import com.senijoshua.swish.databinding.ActivityTeamsBinding

class TeamsActivity : AppCompatActivity() {
    private val binding: ActivityTeamsBinding by lazy { ActivityTeamsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container, TeamsFragment::class.java, intent.extras)
                setReorderingAllowed(true)
                addToBackStack(TeamsFragment::javaClass.name)
            }
        }
    }
}
