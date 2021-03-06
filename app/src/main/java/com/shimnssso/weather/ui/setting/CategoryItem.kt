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
package com.shimnssso.weather.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.R
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.viewmodels.AssetViewModel
import java.io.File

@Composable
fun CategoryItem(
    category: String,
    photos: List<Photo>,
    onFocus: (String) -> Unit,
    onLongPressed: (Photo) -> Unit
) {
    val activity = LocalContext.current as MainActivity
    val imgRes = AssetViewModel.getImage(category)

    LazyRow(
        // modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .width(80.dp)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(imgRes),
                    contentDescription = category,
                    modifier = Modifier
                        .size(40.dp)
                )
                val isWeather = category.startsWith("weather_")
                Text(
                    if (isWeather) category.substring(8) else category.substring(4),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption
                )
            }
        }
        if (photos.isEmpty()) {
            item {
                Image(
                    painter = rememberCoilPainter(
                        request = R.drawable.ella,
                        fadeIn = true,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .alpha(0.5f),
                    contentScale = ContentScale.Crop,
                )
            }
        } else {
            items(photos) { photo ->
                Image(
                    painter = rememberCoilPainter(
                        request = File(photo.path),
                        fadeIn = true,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { onLongPressed(photo) },
                            )
                        },
                    contentScale = ContentScale.Crop,
                )
            }
        }

        item {
            IconButton(
                onClick = {
                    onFocus(category)
                    activity.choosePhotoFromGallery()
                }
            ) {
                Icon(
                    Icons.Outlined.AddPhotoAlternate,
                    contentDescription = null
                )
            }
        }
    }
}
