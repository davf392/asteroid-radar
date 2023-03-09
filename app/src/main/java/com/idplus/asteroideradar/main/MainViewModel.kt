package com.idplus.asteroideradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idplus.asteroideradar.data.remote.model.Asteroid
import com.idplus.asteroideradar.data.remote.model.PictureOfDay
import com.idplus.asteroideradar.data.remote.repositories.AsteroidRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val asteroidRepository: AsteroidRepositoryImpl
)
    : ViewModel() {

    // expose the list of asteroids in a live data to the UI
    val asteroids = asteroidRepository.observeAllAsteroids()

    // expose the picture of the day in a live data to the UI
    private var _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    // expose the event triggered by the user in a live data to the UI
    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?> get() = _navigateToDetailFragment

    private val _downloadInProgress = MutableLiveData<Boolean>()
    val downloadInProgress: LiveData<Boolean> get() = _downloadInProgress

    init {
        _downloadInProgress.value = true
        refreshAsteroidsList()
        refreshPictureOfDayResource()
        _downloadInProgress.value = false
    }

    fun refreshAsteroidsList() = viewModelScope.launch {
        asteroidRepository.refreshAsteroids()
    }

    fun refreshPictureOfDayResource() = viewModelScope.launch {
        _pictureOfDay.value = asteroidRepository.refreshPictureOfDay()
    }

    /**
     *
     */
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

    companion object {
        const val TAG: String = "MainViewModel"
    }
}