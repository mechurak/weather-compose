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
package com.shimnssso.weather.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shimnssso.weather.viewmodels.AssetViewModel
import com.shimnssso.weather.viewmodels.WeatherDay
import com.shimnssso.weather.viewmodels.WeatherHour

@Entity(tableName = "weather_table")
data class DatabaseWeather(
    @PrimaryKey
    val type: String,

    val weatherId: Int,

    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,

    val dt: Int,

    val name: String,
)

@Entity(tableName = "hourly_table")
data class DatabaseHourly(
    @PrimaryKey
    val hourIndex: Int,
    val dt: Int,
    val weatherId: Int,
    val temp: Float,
    val feelsLike: Float,
)

@Entity(tableName = "daily_table")
data class DatabaseDaily(
    @PrimaryKey
    val dayIndex: Int,
    val dt: Int,
    val weatherId: Int,
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseWeather>.asDomainModel(): List<WeatherDay> {
    return map {
        WeatherDay(
            temp = it.temp,
            feelsLike = it.feelsLike,
            tempMin = it.tempMin,
            tempMax = it.tempMax,
            weather = AssetViewModel.getWeatherStr(it.weatherId),
            air = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
            dt = it.dt.toLong()
        )
    }
}

fun DatabaseWeather.asDomainModel(): WeatherDay {
    return WeatherDay(
        temp = temp,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        weather = AssetViewModel.getWeatherStr(weatherId),
        air = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
        dt = dt.toLong()
    )
}

fun List<DatabaseHourly>.asDomainHourly(): List<WeatherHour> {
    return map {
        WeatherHour(
            temp = it.temp,
            weather = AssetViewModel.getWeatherStr(it.weatherId),
            air = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
            dt = it.dt.toLong()
        )
    }
}

fun List<DatabaseDaily>.asDomainDaily(): List<WeatherDay> {
    return map {
        WeatherDay(
            temp = it.temp,
            feelsLike = it.feelsLike,
            tempMin = it.tempMin,
            tempMax = it.tempMax,
            weather = AssetViewModel.getWeatherStr(it.weatherId),
            air = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
            dt = it.dt.toLong()
        )
    }
}
