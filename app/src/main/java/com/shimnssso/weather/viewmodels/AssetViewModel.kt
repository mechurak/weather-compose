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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AssetViewModel : ViewModel() {
    private val _sunnyPhotos = MutableLiveData<List<TempPhoto>>(mutableListOf())
    val sunnyPhotos: LiveData<List<TempPhoto>> = _sunnyPhotos
    private val tempSunnyList = mutableListOf<TempPhoto>()

    fun onImageSaved(imagePath: String) {
        viewModelScope.launch {
            val photo = TempPhoto(category = CATEGORY_WEATHER_SUNNY, path = imagePath)
            tempSunnyList.add(photo)
            val tempList = tempSunnyList.toMutableList()
            _sunnyPhotos.value = tempList
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
