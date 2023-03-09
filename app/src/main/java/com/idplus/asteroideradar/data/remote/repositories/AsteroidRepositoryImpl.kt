package com.idplus.asteroideradar.data.remote.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.idplus.asteroideradar.data.local.dao.AsteroidDao
import com.idplus.asteroideradar.data.remote.api.AsteroidApiService
import com.idplus.asteroideradar.data.remote.api.data.*
import com.idplus.asteroideradar.data.remote.model.Asteroid
import com.idplus.asteroideradar.data.remote.model.PictureOfDay
import com.idplus.asteroideradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AsteroidRepositoryImpl @Inject constructor(
    private val api: AsteroidApiService,
    private val dao: AsteroidDao
)
    : AsteroidRepository {

    override fun observeAllAsteroids(): LiveData<List<Asteroid>> {
        return Transformations.map(dao.getAllAsteroids()) { it.asDomainModel() }
    }

    override suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getAsteroidsByDateAsync(startDate, endDate).await()
                if (response.isSuccessful) {
                    Log.d(TAG, "asteroids list was updated from the server response")
                    dao.insertAllAsteroids(
                        parseAsteroidsJsonResult(
                            response.body()!!
                        ).asDatabaseModel()
                    )
                }
                else
                    Log.d(TAG,
                        "an http error was returned from the server" +
                            " (HTTP ${response.code()} - ${response.message()})")
            }
            catch(e: Exception) {
                Log.d(TAG, "An error occurred while refreshing asteroids : ${e.message}")
            }
        }
    }

    override suspend fun refreshPictureOfDay(): PictureOfDay? {
        var picOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            val response = api.getPictureOfDayAsync().await()
            picOfDay = response.body()!!
        }
        if(picOfDay.mediaType == Constants.IMAGE_MEDIA_TYPE) {
            Log.d("AsteroidRepository", "successful response for picture of the day")
            return picOfDay
        }
        return null
    }

    companion object {
        const val TAG = "AsteroidRepository"
    }
}
