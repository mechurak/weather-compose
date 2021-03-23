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
package com.shimnssso.weather.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shimnssso.weather.database.PhotoDao
import com.shimnssso.weather.utils.Utils

class WeatherViewModel(
    private val dataSource: PhotoDao,
    private val app: Application
) : AndroidViewModel(app) {
    val currentLocation = MutableLiveData<String>(FakeData.location)
    val currentWeather = MutableLiveData<WeatherDay>(FakeData.current)
    val hourlyWeather = MutableLiveData<List<WeatherHour>>(FakeData.hourly)
    val dailyWeather = MutableLiveData<List<WeatherDay>>(FakeData.daily)

    val weatherPhotoList = dataSource.getPhotos(currentWeather.value!!.weather)
    val airPhotoList = dataSource.getPhotos(currentWeather.value!!.air)

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(
        private val dataSource: PhotoDao,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

object FakeData {
    val location = "Suji-gu"

    private val currentDt = System.currentTimeMillis() / 1000
    val current = WeatherDay(
        285.65f,
        283.17f,
        283.15f,
        287.15f,
        AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
        AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
        currentDt
    )
    val daily = listOf(
        WeatherDay(
            284.65f,
            283.17f,
            283.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
            AssetViewModel.CATEGORY_AIR_2_FAIR,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 0 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            283.65f,
            283.17f,
            282.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_4_SNOWY,
            AssetViewModel.CATEGORY_AIR_3_MODERATE,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 1 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            282.65f,
            283.17f,
            281.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
            AssetViewModel.CATEGORY_AIR_4_POOR,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 2 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            285.65f,
            283.17f,
            283.15f,
            288.15f,
            AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
            AssetViewModel.CATEGORY_AIR_5_VERY_POOR,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 3 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            285.65f,
            283.17f,
            283.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_3_RAINY,
            AssetViewModel.CATEGORY_AIR_3_MODERATE,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 4 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            283.65f,
            283.17f,
            281.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
            AssetViewModel.CATEGORY_AIR_2_FAIR,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 5 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            280.65f,
            279.17f,
            279.15f,
            285.15f,
            AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
            AssetViewModel.CATEGORY_AIR_2_FAIR,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 6 * Utils.ONE_DAY_IN_SEC
        ),
        WeatherDay(
            285.65f,
            283.17f,
            283.15f,
            287.15f,
            AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
            AssetViewModel.CATEGORY_AIR_3_MODERATE,
            currentDt - (currentDt % Utils.ONE_DAY_IN_SEC) + 7 * Utils.ONE_DAY_IN_SEC
        )
    )
    val hourly = listOf(
        WeatherHour(
            285.65f,
            AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
            AssetViewModel.CATEGORY_AIR_3_MODERATE,
            currentDt - (currentDt % Utils.ONE_HOUR_IN_SEC)
        ),
        WeatherHour(
            282.12f,
            AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
            AssetViewModel.CATEGORY_AIR_4_POOR,
            currentDt - (currentDt % Utils.ONE_HOUR_IN_SEC) + 1 * 3 * Utils.ONE_HOUR_IN_SEC
        ),
        WeatherHour(
            280.87f,
            AssetViewModel.CATEGORY_WEATHER_3_RAINY,
            AssetViewModel.CATEGORY_AIR_2_FAIR,
            currentDt - (currentDt % Utils.ONE_HOUR_IN_SEC) + 2 * 3 * Utils.ONE_HOUR_IN_SEC
        ),
        WeatherHour(
            279.69f,
            AssetViewModel.CATEGORY_WEATHER_3_RAINY,
            AssetViewModel.CATEGORY_AIR_2_FAIR,
            currentDt - (currentDt % Utils.ONE_HOUR_IN_SEC) + 3 * 3 * Utils.ONE_HOUR_IN_SEC
        ),
        WeatherHour(
            281.34f,
            AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
            AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
            currentDt - (currentDt % Utils.ONE_HOUR_IN_SEC) + 4 * 3 * Utils.ONE_HOUR_IN_SEC
        ),
    )
}