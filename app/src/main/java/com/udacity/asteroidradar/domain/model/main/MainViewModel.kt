package com.udacity.asteroidradar.domain.model.main

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.checkInternetConnection
import com.udacity.asteroidradar.data.AsteroidDatabase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getInstance(application)
    private val repository = Repository(database)

    val pictureOfDay: LiveData<PictureOfDay>
        get() = repository.pictureOfDay

    val asteroids: LiveData<List<Asteroid>>
        get() = repository.asteroidList

    val todayAsteroids: LiveData<List<Asteroid>>
        get() = repository.todayAsteroids

    val asteroidsFromToday: LiveData<List<Asteroid>>
        get() = repository.asteroidsFromToday

    init {
        viewModelScope.launch {
            if (checkInternetConnection(application)) {
                viewModelScope.launch {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        repository.refreshAsteroids()
                    }
                    repository.refreshPicture()
                }
            } else {
                Toast.makeText(
                    application.applicationContext, "Network error",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}