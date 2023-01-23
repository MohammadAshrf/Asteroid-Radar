package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.AsteroidDatabase
import com.udacity.asteroidradar.repository.Repository

class RefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORKER_NAME = "Refresh Worker"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = Repository(database)
        return try {
            repository.refreshPicture()
            repository.refreshAsteroids()
            repository.clearOldData()
            Result.Success()

        } catch (e: Exception) {
            Result.retry()
        }

    }
}