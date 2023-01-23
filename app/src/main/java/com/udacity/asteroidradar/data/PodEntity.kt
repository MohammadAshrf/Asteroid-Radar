package com.udacity.asteroidradar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.api.getEndDate

@Entity(tableName = "pod_entity")
data class PodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val url: String,
    val mediaType: String,
    val title: String,
    val day: String = getEndDate()
)
