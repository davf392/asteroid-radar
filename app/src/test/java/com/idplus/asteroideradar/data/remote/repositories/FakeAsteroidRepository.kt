package com.idplus.asteroideradar.data.remote.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.idplus.asteroideradar.data.remote.model.Asteroid
import com.idplus.asteroideradar.data.remote.model.PictureOfDay

class FakeAsteroidRepository() : AsteroidRepository {

    var shouldReturnPictureOfDayNetworkError = false

    private val asteroidItems = mutableListOf(
        Asteroid(1, "1995 CR", "2025-02-01", 40.1264, 1264.456486, 15.245, 5896523.35, false),
        Asteroid(2, "433 Eros (A898 PA)", "2016-01-31", 10.31, 1264.456486, 15.245, 5896523.35, false),
        Asteroid(3, "719 Albert (A911 TB)", "2027-02-01", 25.42, 1264.456486, 15.245, 5896523.35, false),
        Asteroid(4, "887 Alinda (A918 AA)", "2028-02-01", 80.1264, 1264.456486, 15.245, 5896523.35, false),
        Asteroid(5, "1036 Ganymed (A924 UB)", "2029-02-01", 265.126, 1264.456486, 15.245, 5896523.35, false),
        Asteroid(6, "1221 Amor (1932 EA1)", "2030-02-01", 587.126, 1264.456486, 15.245, 5896523.35, false),
    )

    private val observableAsteroidItems = MutableLiveData<List<Asteroid>>()

    override fun observeAllAsteroids(): LiveData<List<Asteroid>> {
        return observableAsteroidItems
    }

    override suspend fun refreshAsteroids(startDate: String, endDate: String) {
        observableAsteroidItems.postValue(asteroidItems)
    }

    override suspend fun refreshPictureOfDay(): PictureOfDay? {
        return null
    }
}