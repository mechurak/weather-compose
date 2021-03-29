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