package com.idplus.asteroideradar.data.remote.api

import com.idplus.asteroideradar.data.remote.model.PictureOfDay
import com.idplus.asteroideradar.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroidsByDateAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    )
        : Deferred<Response<ResponseBody>>

    @GET("planetary/apod")
    fun getPictureOfDayAsync(
        @Query("api_key") apiKey: String = Constants.API_KEY
    )
        : Deferred<Response<PictureOfDay>>
}