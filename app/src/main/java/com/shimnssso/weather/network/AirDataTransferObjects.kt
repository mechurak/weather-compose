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

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AirResponse(
    val coord: Coord,
    val list: List<AirData>,
)

@JsonClass(generateAdapter = true)
data class Coord(
    val lat: Float,
    val lon: Float,
)

@JsonClass(generateAdapter = true)
data class AirData(
    val main: Main,
    val components: Components,
    val dt: Int,
)

@JsonClass(generateAdapter = true)
data class Main(
    val aqi: Int,
)

@JsonClass(generateAdapter = true)
data class Components(
    val co: Float,
    val no: Float,
    val no2: Float,
    val o3: Float,
    val so2: Float,
    val pm2_5: Float,
    val pm10: Float,
    val nh3: Float
)
