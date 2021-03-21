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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.R
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.viewmodels.AssetViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun SettingScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    navController: NavController? = null,
) {
    Scaffold(
        topBar = { AppBar(navController) }
    ) { innerPadding ->
        val activity = LocalContext.current as MainActivity

        val dataSource = WeatherDatabase.getInstance(activity).photoDao
        val greetingViewModel: AssetViewModel = viewModel(
            factory = AssetViewModel.Factory(dataSource, activity.application)
        )
        val dbSunnyPhotos by greetingViewModel.dbSunnyPhotos.observeAsState(listOf())

        Column {
            Text(text = "size: ${dbSunnyPhotos.size}")
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.ic__01_sunny),
                            contentDescription = "sunny",
                            modifier = Modifier
                                .size(60.dp)
                        )
                        Text("sunny")
                    }
                }
                // val sunnyPhotos = sunnyPhotosStr.split(",")
                items(dbSunnyPhotos) { photo ->
                    Column {
                        CoilImage(
                            data = File(photo.path),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(Modifier.matchParentSize()) {
                                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                                }
                            },
                            fadeIn = true,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Black, CircleShape)
                        )
                    }
                }
                item {
                    Button(
                        onClick = { activity.choosePhotoFromGallery() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Choose from Gallery")
                    }
                }
                item {
                    Button(
                        onClick = { activity.takePhotoFromCamera() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Take photo from Camera")
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBar(navController: NavController?) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController!!.navigate("home") }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(text = "Settings")
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}
