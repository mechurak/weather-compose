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
package com.shimnssso.weather.ui.albumdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.ui.setting.CategoryHeader
import com.shimnssso.weather.viewmodels.AssetViewModel

@Composable
fun AlbumDetailScreen(
    navController: NavController? = null,
    docId: String
) {
    val activity = LocalContext.current as MainActivity

    val viewModel: AlbumDetailViewModel = viewModel(
        factory = AlbumDetailViewModel.Factory(activity.application, docId)
    )

    val isLoading by viewModel.isLoading.observeAsState(true)
    val photos by viewModel.photos.observeAsState()

    Column {
        Spacer(
            Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )

        Scaffold(
            topBar = { AppBar(navController) },
            modifier = Modifier.navigationBarsPadding()
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CategoryHeader(
                    title = "Weather",
                    description = "(designed by Freepik from Flaticon)"
                ) // https://www.flaticon.com/packs/weather-255

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
                    photos = photos?.sunnyImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
                    photos = photos?.cloudyImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_3_RAINY,
                    photos = photos?.rainyImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_4_SNOWY,
                    photos = photos?.snowyImages ?: listOf(),
                )

                CategoryHeader(
                    title = "Air Pollution",
                    description = "(designed by Baianat from Flaticon)"
                ) // https://www.flaticon.com/packs/color-emotions-assets

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
                    photos = photos?.airGoodImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_2_FAIR,
                    photos = photos?.airFairImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_3_MODERATE,
                    photos = photos?.airModerateImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_4_POOR,
                    photos = photos?.airPoorImages ?: listOf(),
                )
                Divider()

                OnlineCategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_5_VERY_POOR,
                    photos = photos?.airVeryPoorImages ?: listOf(),
                )
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun AppBar(
    navController: NavController?
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController!!.navigate("albumList") }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = { /* TODO: */ }
            ) {
                Icon(Icons.Filled.Download, contentDescription = null)
            }
        },
        title = {
            Text(text = "Album Detail")
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}
