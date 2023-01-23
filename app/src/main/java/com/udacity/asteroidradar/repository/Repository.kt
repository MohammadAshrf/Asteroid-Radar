package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getStartDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val database: AsteroidDatabase) {

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.asteroidDao.getPictureOfDay()) { podEntity ->
            podEntity?.asPODModel()
        }

    //todo week filter
    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { asteroidEntities ->
            asteroidEntities?.asAsteroidList()
        }

    //todo today filter
    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroids(getEndDate())) { todayAsteroids ->
            todayAsteroids?.asAsteroidList()
        }


    //todo saved filter
    val asteroidsFromToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsFromToday(getEndDate())) { weekAsteroids ->
            weekAsteroids?.asAsteroidList()
        }


    suspend fun refreshPicture() {
        withContext(Dispatchers.IO) {
            val todayPODinDatabase = database.asteroidDao.checkExistedPod(getEndDate())
            if (todayPODinDatabase == 0) {
                val response = Network.asteroidService.getPictureOfDay()
                if (response.isSuccessful) {
                    response.body()?.let {
                        database.asteroidDao.savePictureOfDay(it.asPODEntity())
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val todayAsteroidsInDb =
                database.asteroidDao.checkExistedAsteroids(getEndDate())
            if (todayAsteroidsInDb == 0) {
                val response = Network.asteroidService.getAsteroids(getStartDate(), getEndDate())
                if (response.isSuccessful) {
                    response.body()?.let {
                        val data = parseAsteroidsJsonResult(JSONObject(it))
                        database.asteroidDao.saveAsteroids(*data.asAsteroidEntityList())
                    }
                }
            }

        }
    }


    suspend fun clearOldData() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePOD(getEndDate())
            database.asteroidDao.deleteAsteroids(getEndDate())
        }
    }
}