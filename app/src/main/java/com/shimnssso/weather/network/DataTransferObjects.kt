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

import com.shimnssso.weather.database.DatabaseDaily
import com.shimnssso.weather.database.DatabaseHourly
import com.shimnssso.weather.database.DatabaseWeather
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OneCallResponse(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    val alerts: Alerts?,
)

@JsonClass(generateAdapter = true)
data class Current(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Float,
    val uvi: Float,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Float,
    val wind_deg: Int,
    val weather: List<Weather>,
)

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
data class Hourly(
    val dt: Int,
    val temp: Float,
    val feels_like: Float,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Float,
    val uvi: Float,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Float,
    val wind_gust: Float?,
    val wind_deg: Int,
    val pop: Float,
    val weather: List<Weather>,
)

@JsonClass(generateAdapter = true)
data class Daily(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Float,
    val wind_speed: Float,
    val wind_gust: Float?,
    val wind_deg: Int,
    val clouds: Int,
    val uvi: Float,
    val pop: Float,
    val rain: Float?,
    val snow: Float?,
    val weather: List<Weather>,
)

@JsonClass(generateAdapter = true)
data class Temp(
    val morn: Float,
    val day: Float,
    val eve: Float,
    val night: Float,
    val min: Float,
    val max: Float
)

@JsonClass(generateAdapter = true)
data class FeelsLike(
    val morn: Float,
    val day: Float,
    val eve: Float,
    val night: Float,
)

@JsonClass(generateAdapter = true)
data class Alerts(
    val sender_name: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String
)

/**
 * Convert Network results to database objects
 */
fun OneCallResponse.asCurrentDatabaseModel(): DatabaseWeather {
    return DatabaseWeather(
        type = "current",
        weatherId = current.weather[0].id,
        temp = current.temp,
        feelsLike = current.feels_like,
        tempMin = current.temp, // TODO: Fix it
        tempMax = current.feels_like, // TODO: Fix it
        dt = current.dt,
        name = "temp" // TODO: Fix it
    )
}

fun OneCallResponse.asDatabaseHourlyList(): List<DatabaseHourly> {
    val databaseHourlyList = mutableListOf<DatabaseHourly>()
    for (i in hourly.indices step 2) {
        if (i > 24) break
        databaseHourlyList.add(
            DatabaseHourly(
                hourIndex = i,
                dt = hourly[i].dt,
                weatherId = hourly[i].weather[0].id,
                temp = hourly[i].temp,
                feelsLike = hourly[i].feels_like
            )
        )
    }
    return databaseHourlyList.toList()
}

fun OneCallResponse.asDatabaseDailyList(): List<DatabaseDaily> {
    val databaseDailyList = mutableListOf<DatabaseDaily>()
    for (i in daily.indices) {
        databaseDailyList.add(
            DatabaseDaily(
                dayIndex = i,
                dt = daily[i].dt,
                weatherId = daily[i].weather[0].id,
                temp = daily[i].temp.day,
                feelsLike = daily[i].feels_like.day,
                tempMin = daily[i].temp.min,
                tempMax = daily[i].temp.max,
            )
        )
    }
    return databaseDailyList.toList()
}
