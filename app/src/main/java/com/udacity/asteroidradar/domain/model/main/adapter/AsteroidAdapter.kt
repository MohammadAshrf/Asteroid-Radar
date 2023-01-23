package com.udacity.asteroidradar.domain.model.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapter(private val asteroidClick: AsteroidClick) :
    ListAdapter<Asteroid, AsteroidsViewHolder>(AsteroidAdapterDiff()) {

    private lateinit var binding: AsteroidItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidsViewHolder.LAYOUT, parent, false
        )
        return AsteroidsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        holder.binding.also {
            it.asteroid = getItem(position)
            it.asteroidClick = asteroidClick
        }
    }
}

class AsteroidClick(val block: (Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = block(asteroid)
}