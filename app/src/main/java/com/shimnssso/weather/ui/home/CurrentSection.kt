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
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
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
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shimnssso.weather.R
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.viewmodels.AssetViewModel
import com.shimnssso.weather.viewmodels.WeatherDay
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrentSection(
    location: String,
    weather: WeatherDay,
    weatherPhotoList: List<Photo>,
    airPhotoList: List<Photo>
) {
    val sdf = SimpleDateFormat("EEE, d MMM HH:mm", Locale.ENGLISH) // Wed, 4 Jul 12:08
    val date = Date(weather.dt * 1000)
    val photoList = (weatherPhotoList + airPhotoList).shuffled()
    var photoIndex by remember { mutableStateOf(0) }

    val bottomPadding: Dp by animateDpAsState(
        targetValue = 30.dp,
        // configure the animation duration and easing
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                0.dp at 0 with FastOutSlowInEasing // for 0-15 ms
                // 60.dp at 500 with FastOutSlowInEasing // for 15-75 ms
                // 0.dp at 1000 // ms
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    val weatherHeight: Dp by animateDpAsState(
        targetValue = 80.dp,
        // configure the animation duration and easing
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                60.dp at 0 with FastOutSlowInEasing // for 0-15 ms
                80.dp at 250
                // 0.dp at 1000 // ms
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
            CoilImage(
                data = R.drawable.ella,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.matchParentSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                },
                fadeIn = true,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterStart)
            )
        } else {
            CoilImage(
                data = File(photoList[photoIndex].path),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(Modifier.matchParentSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                },
                fadeIn = true,
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
                    }
            )
        }

        CoilImage(
            data = AssetViewModel.getImage(weather.weather),
            contentDescription = weather.weather,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(bottom = bottomPadding)
                .width(80.dp)
                .height(weatherHeight)
                .align(Alignment.BottomStart)
        )

        CoilImage(
            data = AssetViewModel.getImage(weather.air),
            contentDescription = weather.air,
            modifier = Modifier
                .padding(start = 60.dp)
                .size(60.dp)
                .align(Alignment.BottomCenter)
        )

        Text(
            location, modifier = Modifier
                .align(Alignment.TopStart)
        )

        Text(
            text = sdf.format(date),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp)
        )

        Column(modifier = Modifier.align(Alignment.CenterEnd)) {
            Text("16º")
            Text("18º/3º")
            Text("Sunny")
            Text("Very Good")
        }
    }
}
