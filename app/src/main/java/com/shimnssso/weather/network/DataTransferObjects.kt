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
package com.shimnssso.weather.network

import com.shimnssso.weather.database.DatabaseWeather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: Sys,
    val id: Int,
    val name: String,
    val cod: Int
)

@JsonClass(generateAdapter = true)
data class Coord(
    val lon: Double,
    val lat: Double
)

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
data class Main(
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val temp_min: Float,
    val temp_max: Float
)

@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Double,
    val deg: Int
)

@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)

@JsonClass(generateAdapter = true)
data class Sys(
    val type: Int,
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

/**
 * Convert Network results to database objects
 */
fun WeatherResponse.asDatabaseModel(): DatabaseWeather {
    return DatabaseWeather(
        type = "current",
        weatherId = weather[0].id,
        temp = main.temp,
        feelsLike = main.feels_like,
        tempMin = main.temp_min,
        tempMax = main.temp_max,
        dt = dt,
        name = name
    )
}
