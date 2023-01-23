package com.udacity.asteroidradar.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAsteroids(vararg asteroidEntity: AsteroidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePictureOfDay(podEntity: PodEntity)

    @Query("SELECT count(*) FROM asteroid_entity WHERE date(:currentDay) = date(day)")
    fun checkExistedAsteroids(currentDay: String): Int

    @Query("SELECT * FROM asteroid_entity ORDER BY close_approach_date DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE close_approach_date =:currentDay")
    fun getTodayAsteroids(currentDay: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_entity WHERE close_approach_date >=:currentDay ORDER BY close_approach_date DESC")
    fun getAsteroidsFromToday(currentDay: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT count(*) FROM pod_entity WHERE date(:currentDay) = date(day)")
    fun checkExistedPod(currentDay: String): Int

    @Query("SELECT * FROM pod_entity ORDER BY day DESC LIMIT 1")
    fun getPictureOfDay(): LiveData<PodEntity>

    @Query("DELETE FROM pod_entity WHERE date(:currentDay) > date(day)")
    fun deletePOD(currentDay: String)

    @Query("DELETE FROM asteroid_entity WHERE date(:currentDay) > date(day)")
    fun deleteAsteroids(currentDay: String)

}
