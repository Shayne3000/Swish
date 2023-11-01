package com.senijoshua.swish.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.senijoshua.swish.data.Teams
import com.senijoshua.swish.databinding.LayoutMainItemBinding

class MainAdapter(private val context: Context) :
    ListAdapter<Teams, MainAdapter.MainViewHolder>(MainDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutMainItemBinding.inflate(layoutInflater, parent, false)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MainViewHolder(private val binding: LayoutMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Teams) {
            binding.teamName.text = team.name
            team.logo?.let { thumbnailUrl ->
                Glide.with(context).load(thumbnailUrl).circleCrop().into(binding.teamThumbnail)
            }
        }
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
