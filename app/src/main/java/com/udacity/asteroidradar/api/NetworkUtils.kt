package com.udacity.asteroidradar.api

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.util.Constants.DEFAULT_END_DATE_DAYS
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.N)
fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.has(formattedDate)) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

@RequiresApi(Build.VERSION_CODES.N)
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun getStartDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
    val current = calendar.time
    val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    return end.format(current)
}

fun getEndDate(): String {
    val start = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    val current = Calendar.getInstance().time
    return start.format(current)
}

@SuppressLint("SuspiciousIndentation")
fun checkInternetConnection(application: Application): Boolean {
    val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork!!.isConnected
}
