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

class WeatherViewModel(
    private val dataSource: PhotoDao,
    private val app: Application
) : AndroidViewModel(app) {
    val currentLocation = MutableLiveData<String>("Suji-gu")
    val todayWeather = MutableLiveData<Weather>(
        Weather(
            time = System.currentTimeMillis(),
            weather = AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
            air = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD
        )
    )

    val weatherPhotoList = dataSource.getPhotos(todayWeather.value!!.weather)
    val airPhotoList = dataSource.getPhotos(todayWeather.value!!.air)

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
