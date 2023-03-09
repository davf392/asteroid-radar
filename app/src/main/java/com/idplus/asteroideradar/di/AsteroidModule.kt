package com.idplus.asteroideradar.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.idplus.asteroideradar.data.local.AsteroidDatabase
import com.idplus.asteroideradar.data.local.dao.AsteroidDao
import com.idplus.asteroideradar.data.remote.api.AsteroidApiService
import com.idplus.asteroideradar.data.remote.repositories.AsteroidRepositoryImpl
import com.idplus.asteroideradar.utils.Constants
import com.idplus.asteroideradar.utils.Constants.DATABASE_NAME
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AsteroidModule: Application() {

    @Provides
    @Singleton
    fun provideAsteroidApi(): AsteroidApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(AsteroidApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAsteroidDatabase(@ApplicationContext context: Context): AsteroidDatabase
        = Room.databaseBuilder(context, AsteroidDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideAsteroidDao(database: AsteroidDatabase): AsteroidDao =
        database.asteroidDao()

    @Provides
    @Singleton
    fun provideAsteroidRepository(api: AsteroidApiService, dao: AsteroidDao) =
        AsteroidRepositoryImpl(api, dao)
}