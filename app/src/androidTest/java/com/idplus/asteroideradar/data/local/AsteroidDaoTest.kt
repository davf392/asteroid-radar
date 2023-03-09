package com.idplus.asteroideradar.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.collect.Iterables
import com.google.common.truth.Truth.assertThat
import com.idplus.asteroideradar.data.local.dao.AsteroidDao
import com.idplus.asteroideradar.data.local.model.AsteroidItem
import com.idplus.asteroideradar.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AsteroidDaoTest {

    @get:Rule
    var instantTaskException = InstantTaskExecutorRule()

    private lateinit var database: AsteroidDatabase
    private lateinit var dao: AsteroidDao

    @Before
    fun setup() {
        database =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AsteroidDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        dao = database.asteroidDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAsteroidItems() = runBlockingTest {
        val testAsteroids = mutableListOf(
            AsteroidItem(1, "1995 CR", "2025-02-01", 40.1264, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(2, "433 Eros (A898 PA)", "2016-01-31", 10.31, 1264.456486, 15.245, 5896523.35, true),
            AsteroidItem(3, "719 Albert (A911 TB)", "2027-02-01", 25.42, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(4, "887 Alinda (A918 AA)", "2028-02-01", 80.1264, 1264.456486, 15.245, 5896523.35, true),
            AsteroidItem(5, "1036 Ganymed (A924 UB)", "2029-02-01", 265.126, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(6, "1221 Amor (1932 EA1)", "2030-02-01", 587.126, 1264.456486, 15.245, 5896523.35, true)
        )
        dao.insertAllAsteroids(testAsteroids)

        testAsteroids.sortBy { it.closeApproachDate }

        assertThat(Iterables.elementsEqual(testAsteroids, dao.getAllAsteroids().getOrAwaitValue()))
    }

    @Test
    fun testDeleteAsteroidItem() = runBlockingTest {
        val asteroidItem =
            AsteroidItem(
                1,
                "1995 CR",
                "2025-02-01",
                40.1264,
                1264.456486,
                15.245,
                5896523.35,
                false
            )
        dao.insertAsteroid(asteroidItem)
        dao.deleteAsteroid(asteroidItem)

        val allAsteroids = dao.getAllAsteroids().getOrAwaitValue()
        assertThat(allAsteroids).doesNotContain(asteroidItem)
    }

    @Test
    fun testGetTotalPotentiallyHazardousAsteroids() = runBlockingTest {
        val testAsteroids = mutableListOf(
            AsteroidItem(1, "1995 CR", "2025-02-01", 40.1264, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(2, "433 Eros (A898 PA)", "2016-01-31", 10.31, 1264.456486, 15.245, 5896523.35, true),
            AsteroidItem(3, "719 Albert (A911 TB)", "2027-02-01", 25.42, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(4, "887 Alinda (A918 AA)", "2028-02-01", 80.1264, 1264.456486, 15.245, 5896523.35, true),
            AsteroidItem(5, "1036 Ganymed (A924 UB)", "2029-02-01", 265.126, 1264.456486, 15.245, 5896523.35, false),
            AsteroidItem(6, "1221 Amor (1932 EA1)", "2030-02-01", 587.126, 1264.456486, 15.245, 5896523.35, true)
        )
        dao.insertAllAsteroids(testAsteroids)

        var testTotalHazardousAsteroids = 0
        testAsteroids.forEach { if(it.potentiallyHazardous) testTotalHazardousAsteroids += 1 }
        val cachedTotalHazardousAsteroids = dao.getTotalPotentiallyHazardousAsteroids()

        assertThat(testTotalHazardousAsteroids).isEqualTo(cachedTotalHazardousAsteroids)
    }
}