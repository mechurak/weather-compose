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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.database.PhotoDao
import kotlinx.coroutines.launch

class AssetViewModel(
    private val dataSource: PhotoDao,
    application: Application
) : AndroidViewModel(application) {
    val dbSunnyPhotos = dataSource.getPhotos(CATEGORY_WEATHER_SUNNY)

    fun onImageSaved(imagePath: String) {
        viewModelScope.launch {
            val photo = Photo(category = CATEGORY_WEATHER_SUNNY, path = imagePath)
            dataSource.insert(photo)
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
            if (modelClass.isAssignableFrom(AssetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AssetViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    companion object {
        const val CATEGORY_WEATHER_SUNNY = "sunny"
        const val CATEGORY_WEATHER_CLOUDY = "cloudy"
        const val CATEGORY_WEATHER_RAINY = "rainy"
        const val CATEGORY_WEATHER_SNOWY = "snowy"
        const val CATEGORY_AIR_VERY_GOOD = "air_very_good"
        const val CATEGORY_AIR_FAIR = "air_fair"
        const val CATEGORY_AIR_MODERATE = "air_moderate"
        const val CATEGORY_AIR_POOR = "air_poor"
        const val CATEGORY_AIR_VERY_POOR = "air_very_poor"
    }
}
