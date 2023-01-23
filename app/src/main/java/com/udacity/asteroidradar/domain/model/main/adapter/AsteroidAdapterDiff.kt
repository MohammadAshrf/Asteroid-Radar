package com.udacity.asteroidradar.domain.model.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapterDiff : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }


}
