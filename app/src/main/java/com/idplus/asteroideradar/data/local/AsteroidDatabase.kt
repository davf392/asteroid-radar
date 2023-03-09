package com.idplus.asteroideradar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idplus.asteroideradar.data.local.dao.AsteroidDao
import com.idplus.asteroideradar.data.local.model.AsteroidItem

@Database(
    entities = [AsteroidItem::class],
    version = 1
)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao
}