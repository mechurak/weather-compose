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
import androidx.annotation.DrawableRes
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shimnssso.weather.R
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.database.PhotoDao
import com.shimnssso.weather.firebase.MyFirebase
import com.shimnssso.weather.utils.CUR_CATEGORY
import com.shimnssso.weather.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AssetViewModel(
    private val dataSource: PhotoDao,
    private val app: Application
) : AndroidViewModel(app) {
    val sunnyPhotos = dataSource.getPhotos(CATEGORY_WEATHER_1_SUNNY)
    val cloudyPhotos = dataSource.getPhotos(CATEGORY_WEATHER_2_CLOUDY)
    val rainyPhotos = dataSource.getPhotos(CATEGORY_WEATHER_3_RAINY)
    val snowyPhotos = dataSource.getPhotos(CATEGORY_WEATHER_4_SNOWY)

    val airVeryGoodPhotos = dataSource.getPhotos(CATEGORY_AIR_1_VERY_GOOD)
    val airFairPhotos = dataSource.getPhotos(CATEGORY_AIR_2_FAIR)
    val airModeratePhotos = dataSource.getPhotos(CATEGORY_AIR_3_MODERATE)
    val airPoorPhotos = dataSource.getPhotos(CATEGORY_AIR_4_POOR)
    val airVeryPoorPhotos = dataSource.getPhotos(CATEGORY_AIR_5_VERY_POOR)

    private var _currentUserId = MutableLiveData<String>("")
    val currentUserId: LiveData<String>
        get() = _currentUserId

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        _currentUserId.value = Firebase.auth.currentUser?.uid ?: ""
    }

    fun onImageSaved(imagePath: String) {
        viewModelScope.launch {
            val curCategoryFlow: Flow<String> = app.dataStore.data.map { preferences ->
                preferences[CUR_CATEGORY] ?: "undefined"
            }

            val photo = Photo(category = curCategoryFlow.first(), path = imagePath)
            dataSource.insert(photo)
        }
    }

    fun changeCurCategory(curCategory: String) {
        viewModelScope.launch {
            app.dataStore.edit { settings ->
                settings[CUR_CATEGORY] = curCategory
            }
        }
    }

    fun removeImage(photo: Photo) {
        viewModelScope.launch {
            val file = File(photo.path)
            val ret = file.delete()
            Timber.i("removeImage(). ret: $ret")
            dataSource.remove(photo.photoId)
        }
    }

    fun uploadAlbum(title: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val photos = dataSource.getAllPhotos()
            Timber.i("photos.size: ${photos.size}")
            MyFirebase.uploadAlbum(title, photos)
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
            if (modelClass.isAssignableFrom(AssetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AssetViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    companion object {
        const val CATEGORY_WEATHER_1_SUNNY = "weather_sunny"
        const val CATEGORY_WEATHER_2_CLOUDY = "weather_cloudy"
        const val CATEGORY_WEATHER_3_RAINY = "weather_rainy"
        const val CATEGORY_WEATHER_4_SNOWY = "weather_snowy"
        const val CATEGORY_AIR_1_VERY_GOOD = "air_very_good"
        const val CATEGORY_AIR_2_FAIR = "air_fair"
        const val CATEGORY_AIR_3_MODERATE = "air_moderate"
        const val CATEGORY_AIR_4_POOR = "air_poor"
        const val CATEGORY_AIR_5_VERY_POOR = "air_very_poor"

        @DrawableRes
        fun getImage(category: String): Int {
            return when (category) {
                CATEGORY_WEATHER_1_SUNNY -> R.drawable.ic__01_sunny
                CATEGORY_WEATHER_2_CLOUDY -> R.drawable.ic__02_cloudy
                CATEGORY_WEATHER_3_RAINY -> R.drawable.ic__03_rain
                CATEGORY_WEATHER_4_SNOWY -> R.drawable.ic__09_snow
                CATEGORY_AIR_1_VERY_GOOD -> R.drawable.air_1_very_good_28_happy
                CATEGORY_AIR_2_FAIR -> R.drawable.air_2_fair_47_happy
                CATEGORY_AIR_3_MODERATE -> R.drawable.air_3_moderate_04_surprised
                CATEGORY_AIR_4_POOR -> R.drawable.air_4_poor_13_nervous
                CATEGORY_AIR_5_VERY_POOR -> R.drawable.air_5_very_poor_36_devil
                else -> R.drawable.ella
            }
        }

        fun getWeatherStr(weatherId: Int): String {
            return when (weatherId) {
                in 200..599 -> CATEGORY_WEATHER_3_RAINY
                in 600..699 -> CATEGORY_WEATHER_4_SNOWY
                in 800..801 -> CATEGORY_WEATHER_1_SUNNY
                in 802..804 -> CATEGORY_WEATHER_2_CLOUDY
                else -> CATEGORY_WEATHER_1_SUNNY
            }
        }

        fun getAirStr(aqi: Int): String {
            return when (aqi) {
                1 -> CATEGORY_AIR_1_VERY_GOOD
                2 -> CATEGORY_AIR_2_FAIR
                3 -> CATEGORY_AIR_3_MODERATE
                4 -> CATEGORY_AIR_4_POOR
                5 -> CATEGORY_AIR_5_VERY_POOR
                else -> CATEGORY_AIR_3_MODERATE
            }
        }
    }
}
