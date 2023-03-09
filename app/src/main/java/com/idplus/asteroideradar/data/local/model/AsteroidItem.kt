package com.idplus.asteroideradar.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var name: String,
    var closeApproachDate: String,
    var absoluteMagnitude: Double,
    var estimatedDiameter: Double,
    var relativeVelocity: Double,
    var distanceFromEarth: Double,
    var potentiallyHazardous: Boolean
)
