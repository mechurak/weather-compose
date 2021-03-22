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

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shimnssso.weather.R
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.viewmodels.Weather
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun BriefInfo(
    location: String,
    weather: Weather,
    weatherPhotoList: List<Photo>,
    airPhotoList: List<Photo>
) {
    Text(location)
    Text(weather.time.toString())

    val photoList = (weatherPhotoList + airPhotoList).shuffled()
    var photoIndex by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(240.dp)
                .height(200.dp)
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
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .align(Alignment.Center)
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
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .align(Alignment.Center)
                        .clickable {
                            photoIndex++
                            if (photoIndex >= photoList.size) {
                                photoIndex = 0
                            }
                        }
                )
            }

            Image(
                painter = painterResource(R.drawable.ic__01_sunny),
                contentDescription = "sunny",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart)
            )
            Image(
                painter = painterResource(R.drawable.air_1_very_good_28_happy),
                contentDescription = "very_good",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        Column {
            Text("16º")
            Text("18º/3º")
            Text("Sunny")
            Text("Very Good")
        }
    }
}
