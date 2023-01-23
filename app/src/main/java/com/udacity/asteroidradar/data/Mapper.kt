package com.udacity.asteroidradar.data

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

//TODO Extension functions map between room entity and kotlin model

fun AsteroidEntity.asAsteroidModel(): Asteroid {
    return Asteroid(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}

fun List<Asteroid>.asAsteroidEntityList(): Array<AsteroidEntity> {
    return map { asteroid ->
        AsteroidEntity(
            id = asteroid.id,
            codename = asteroid.codename,
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous,
            closeApproachDate = asteroid.closeApproachDate,
            relativeVelocity = asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth
        )
    }.toTypedArray()

}

fun List<AsteroidEntity>.asAsteroidList(): List<Asteroid> {
    return map { asteroidEntity ->
        asteroidEntity.asAsteroidModel()
    }
}

fun PictureOfDay.asPODEntity(): PodEntity {
    return PodEntity(
        url = url,
        title = title,
        mediaType = mediaType
    )
}

fun PodEntity.asPODModel(): PictureOfDay {
    return PictureOfDay(
        url = url,
        title = title,
        mediaType = mediaType
    )
}