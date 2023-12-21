package com.senijoshua.swish.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.senijoshua.swish.R
import com.senijoshua.swish.data.Teams
import com.senijoshua.swish.databinding.LayoutMainItemBinding

class MainAdapter : ListAdapter<Teams, MainViewHolder>(MainDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainItemBinding.inflate(layoutInflater, parent, false)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MainViewHolder(private val binding: LayoutMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(team: Teams) {
        binding.teamName.text = team.name

        team.logo?.let { thumbnailUrl ->
            loadFromUrl(thumbnailUrl)
        } ?: loadFromLocalResource()
    }

    private fun loadFromUrl(thumbnailUrl: String) {
        Glide.with(binding.root.context)
            .load(thumbnailUrl)
            .placeholder(
                ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.placeholder
                )
            )
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.teamThumbnail)
    }

    private fun loadFromLocalResource() {
        Glide.with(binding.root.context)
            .load(ContextCompat.getDrawable(binding.root.context, R.drawable.placeholder))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.teamThumbnail)
    }
}

object MainDiffUtil : DiffUtil.ItemCallback<Teams>() {
    override fun areItemsTheSame(oldItem: Teams, newItem: Teams): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Teams, newItem: Teams): Boolean {
        return oldItem.name == newItem.name && oldItem.logo == newItem.logo
    }
}
