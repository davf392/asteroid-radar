package com.idplus.asteroideradar.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.idplus.asteroideradar.data.local.model.AsteroidItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<AsteroidItem>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAllAsteroids() : LiveData<List<AsteroidItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(asteroids: List<AsteroidItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroid(asteroid: AsteroidItem)

    @Delete
    suspend fun deleteAsteroid(asteroid: AsteroidItem)

    @Query("SELECT COUNT(*) FROM asteroids WHERE potentiallyHazardous = 1")
    suspend fun getTotalPotentiallyHazardousAsteroids(): Int
}