package com.udacity.asteroidradar.domain.model.main.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidsViewHolder(
    val binding: AsteroidItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.asteroid_item
    }
}
