/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shimnssso.weather.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.shimnssso.weather.database.DatabaseWeather
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.database.asDomainDaily
import com.shimnssso.weather.database.asDomainHourly
import com.shimnssso.weather.database.asDomainModel
import com.shimnssso.weather.network.WeatherNetwork
import com.shimnssso.weather.network.asCurrentDatabaseModel
import com.shimnssso.weather.network.asDatabaseDailyList
import com.shimnssso.weather.network.asDatabaseHourlyList
import com.shimnssso.weather.viewmodels.FakeData
import com.shimnssso.weather.viewmodels.WeatherDay
import com.shimnssso.weather.viewmodels.WeatherHour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */
class WeatherRepository(private val database: WeatherDatabase) {

    private val _current: LiveData<DatabaseWeather?> = database.weatherDao.getWeather("current")

    val todayWeather: LiveData<WeatherDay> =
        Transformations.map(_current) {
            it?.asDomainModel() ?: FakeData.current
        }

    val weatherPhotoList: LiveData<List<Photo>> =
        Transformations.switchMap(todayWeather) {
            database.photoDao.getPhotos(it.weather)
        }

    val airPhotoList: LiveData<List<Photo>> =
        Transformations.switchMap(todayWeather) {
            database.photoDao.getPhotos(it.air)
        }

    val hourlies: LiveData<List<WeatherHour>> = Transformations.map(database.weatherDao.getHourlies()) {
        it.asDomainHourly()
    }

    val dailies: LiveData<List<WeatherDay>> = Transformations.map(database.weatherDao.getDailies()) {
        it.asDomainDaily()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            Log.i("WeatherRepository", "refresh videos is called")
            val oneCallResponse = WeatherNetwork.openWeather.getWeather(
                lat = 37.32971822303459,
                lon = 127.11430926761564,
                exclude = "minutely",
                units = "metric",
                appid = "e4b94150e27b4a33b5e5c2b05467a22f"
            )
            Log.i("WeatherRepository", "oneCallResponse: $oneCallResponse")

            val sdf = SimpleDateFormat("EEE, d MMM HH:mm", Locale.ENGLISH) // Wed, 4 Jul 12:08
            val current = oneCallResponse.current
            val daily = oneCallResponse.daily
            val hourly = oneCallResponse.hourly

            Log.i("WeatherRepository", "current: ${sdf.format(Date(current.dt * 1000L))}")

            for ((idx, item) in hourly.withIndex()) {
                Log.i("WeatherRepository", "hourly[$idx]: ${sdf.format(Date(item.dt * 1000L))}")
            }

            for ((idx, item) in daily.withIndex()) {
                Log.i("WeatherRepository", "daily[$idx]: ${sdf.format(Date(item.dt * 1000L))} ${item.dt}")
            }

            val airCurrentResponse = WeatherNetwork.openWeather.getAirCurrent(
                lat = 37.32971822303459,
                lon = 127.11430926761564,
                appid = "e4b94150e27b4a33b5e5c2b05467a22f"
            )
            Log.i("WeatherRepository", "airCurrentResponse: $oneCallResponse")
            for ((idx, item) in airCurrentResponse.list.withIndex()) {
                Log.i("WeatherRepository", "current list[$idx]: ${sdf.format(Date(item.dt * 1000L))}")
            }
            database.weatherDao.insert(oneCallResponse.asCurrentDatabaseModel(airCurrentResponse))


            val airForecastResponse = WeatherNetwork.openWeather.getAirForecast(
                lat = 37.32971822303459,
                lon = 127.11430926761564,
                appid = "e4b94150e27b4a33b5e5c2b05467a22f"
            )
            Log.i("WeatherRepository", "airForecastResponse: $oneCallResponse")
            for ((idx, item) in airForecastResponse.list.withIndex()) {
                Log.i("WeatherRepository", "current list[$idx]: ${sdf.format(Date(item.dt * 1000L))}")
            }
            database.weatherDao.insertHourlies(oneCallResponse.asDatabaseHourlyList(airForecastResponse))
            database.weatherDao.insertDailies(oneCallResponse.asDatabaseDailyList(airForecastResponse))
        }
    }
}
