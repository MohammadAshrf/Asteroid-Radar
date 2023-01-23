package com.udacity.asteroidradar.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class, PodEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE as AsteroidDatabase
            }
        }
    }
}
