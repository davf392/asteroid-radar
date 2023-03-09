package com.idplus.asteroideradar.data.remote.api.data

import com.idplus.asteroideradar.data.local.model.AsteroidItem
import com.idplus.asteroideradar.data.remote.model.Asteroid
import com.idplus.asteroideradar.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(response: ResponseBody): ArrayList<Asteroid> {
    val jsonResult = JSONObject(response.string())
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.optJSONArray(formattedDate) != null) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
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

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        formattedDateList.add(formatDate(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

private fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}

fun getToday(): String {

    return "2023-01-20"
}

fun getSeventhDay(): String {

    return "2023-01-27"
}

fun ArrayList<Asteroid>.asDatabaseModel(): List<AsteroidItem> {
    return map {
        AsteroidItem(
            id = it.id.toInt(),
            name = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            potentiallyHazardous = it.potentiallyHazardous
        )
    }
    .toList()
}

fun List<AsteroidItem>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id!!.toLong(),
            codename = it.name,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            potentiallyHazardous = it.potentiallyHazardous
        )
    }
}
