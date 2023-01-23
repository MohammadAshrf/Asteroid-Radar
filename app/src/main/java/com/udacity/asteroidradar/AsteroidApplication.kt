package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    private fun setupWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()
        val refreshRequest = PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints).build()
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                RefreshWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                refreshRequest
            )

    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            setupWorker()
        }
    }


}