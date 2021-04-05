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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shimnssso.weather.database.PhotoDao
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.repository.WeatherRepository
import com.shimnssso.weather.utils.Utils
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class WeatherViewModel(
    private val dataSource: PhotoDao,
    private val app: Application
) : AndroidViewModel(app) {

    val currentLocation = MutableLiveData<String>(FakeData.location)

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val weatherRepository = WeatherRepository(WeatherDatabase.getInstance(app))

    val todayWeather = weatherRepository.todayWeather
    val weatherPhotoList = weatherRepository.weatherPhotoList
    val airPhotoList = weatherRepository.airPhotoList

    val hourlyWeather = weatherRepository.hourlies
    val dailyWeather = weatherRepository.dailies

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshDataFromRepository() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                weatherRepository.refreshWeather()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                Timber.e(networkError.toString())
                networkError.printStackTrace()
                // Show a Toast error message and hide the progress bar.
                // if (curWeather.value.isNullOrEmpty())
                //     _eventNetworkError.value = true
            }
            _isLoading.value = false
        }
    }

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
