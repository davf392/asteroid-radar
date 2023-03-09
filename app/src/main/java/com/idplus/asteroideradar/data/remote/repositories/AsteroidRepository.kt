package com.idplus.asteroideradar.data.remote.repositories

import androidx.lifecycle.LiveData
import com.idplus.asteroideradar.data.remote.api.data.getSeventhDay
import com.idplus.asteroideradar.data.remote.api.data.getToday
import com.idplus.asteroideradar.data.remote.model.Asteroid
import com.idplus.asteroideradar.data.remote.model.PictureOfDay

interface AsteroidRepository {

    fun observeAllAsteroids(): LiveData<List<Asteroid>>

    suspend fun refreshAsteroids(startDate: String = getToday(), endDate: String = getSeventhDay())

    suspend fun refreshPictureOfDay(): PictureOfDay?
}