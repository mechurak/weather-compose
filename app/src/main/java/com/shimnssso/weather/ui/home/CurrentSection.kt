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
package com.shimnssso.weather.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.shimnssso.weather.R
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.utils.Utils
import com.shimnssso.weather.viewmodels.AssetViewModel
import com.shimnssso.weather.viewmodels.WeatherDay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentSection(
    location: String,
    weather: WeatherDay,
    weatherPhotoList: List<Photo> = listOf(),
    airPhotoList: List<Photo> = listOf()
) {
    val sdf = SimpleDateFormat("EEE, d MMM HH:mm", Locale.ENGLISH) // Wed, 4 Jul 12:08
    val date = Date(weather.dt * 1000)
    val photoList = remember(weatherPhotoList, airPhotoList) { (weatherPhotoList + airPhotoList).shuffled() }
    var photoIndex by remember { mutableStateOf(0) }
    val isWeather = photoList.isNotEmpty() && photoList[photoIndex].category.startsWith("weather_")

    val infiniteTransition = rememberInfiniteTransition()
    val bottomPadding by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                0f at 0 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val weatherHeight by infiniteTransition.animateFloat(
        initialValue = 60f,
        targetValue = 80f,
        // configure the animation duration and easing
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                60f at 0 with FastOutSlowInEasing
                80f at 250
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val airHeight by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 60f,
        // configure the animation duration and easing
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                40f at 0 with FastOutSlowInEasing
                60f at 250
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .height(250.dp)
            .fillMaxWidth()
    ) {
        if (photoList.isEmpty()) {
            Image(
                painter = rememberCoilPainter(
                    request = R.drawable.ella,
                    fadeIn = true,
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterStart),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painter = rememberCoilPainter(
                    request = File(photoList[photoIndex].path),
                    fadeIn = true,
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterStart)
                    .clickable {
                        photoIndex++
                        if (photoIndex >= photoList.size) {
                            photoIndex = 0
                        }
                    },
                contentScale = ContentScale.Crop,
            )
        }

        val curWeatherPadding by animateDpAsState(if (isWeather) bottomPadding.dp else 0.dp)
        val curWeatherHeight by animateDpAsState(if (isWeather) weatherHeight.dp else 80.dp)
        Image(
            painter = rememberCoilPainter(
                request = AssetViewModel.getImage(weather.weather),
            ),
            contentDescription = weather.weather,
            modifier = Modifier
                .padding(bottom = curWeatherPadding)
                .width(80.dp)
                .height(curWeatherHeight)
                .align(Alignment.BottomStart),
            contentScale = ContentScale.FillBounds,
        )

        val curAirPadding by animateDpAsState(if (isWeather && photoList.isNotEmpty()) 0.dp else bottomPadding.dp)
        val curAirHeight by animateDpAsState(if (isWeather && photoList.isNotEmpty()) 60.dp else airHeight.dp)
        Image(
            painter = rememberCoilPainter(
                request = AssetViewModel.getImage(weather.air),
            ),
            contentDescription = weather.air,
            modifier = Modifier
                .padding(
                    start = 60.dp,
                    bottom = curAirPadding
                )
                .width(60.dp)
                .height(curAirHeight)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds,
        )

        Text(
            location,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .align(Alignment.TopStart)
        )

        Text(
            text = sdf.format(date),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp)
        )

        val temperature = Utils.getTemp(weather.temp)
        val maxTemperature = Utils.getTemp(weather.tempMax)
        val minTemperature = Utils.getTemp(weather.tempMin)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text(
                "${temperature}??",
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                "${maxTemperature}??/${minTemperature}??",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary,
            )
            Text(
                "weather",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                weather.weather.substring(8).capitalize(Locale.ROOT),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary,
            ) // TODO: Add description
            Text(
                "air",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                weather.air.substring(4).capitalize(Locale.ROOT),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary,
            )
        }
    }
}
