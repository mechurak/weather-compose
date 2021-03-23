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

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.viewmodels.FakeData
import com.shimnssso.weather.viewmodels.WeatherViewModel
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun HomeScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    navController: NavController? = null
) {
    val activity = LocalContext.current as MainActivity

    val dataSource = WeatherDatabase.getInstance(activity).photoDao
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModel.Factory(dataSource, activity.application)
    )

    val location by weatherViewModel.currentLocation.observeAsState("no location")
    val todayWeather by weatherViewModel.currentWeather.observeAsState(FakeData.current)
    val weatherPhotoList by weatherViewModel.weatherPhotoList.observeAsState(listOf())
    val airPhotoList by weatherViewModel.weatherPhotoList.observeAsState(listOf())

    val hourlyWeather by weatherViewModel.hourlyWeather.observeAsState(FakeData.hourly)
    val dayliyWeather by weatherViewModel.dailyWeather.observeAsState(FakeData.daily)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .align(Alignment.TopEnd)
            ) {
                IconButton(onClick = { navController!!.navigate("setting") }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = { /* TODO: Refresh weather data */ }) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            CurrentSection(
                location = location,
                weather = todayWeather,
                weatherPhotoList = weatherPhotoList,
                airPhotoList = airPhotoList
            )
        }
        Divider(
            color = MaterialTheme.colors.primaryVariant,
            thickness = 2.dp
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .navigationBarsPadding()
        ) {
            HourlySection(weatherList = hourlyWeather)
            Divider(Modifier.padding(vertical = 12.dp))

            DailySection(weatherList = dayliyWeather)
            Divider(Modifier.padding(vertical = 12.dp))
            Text("designed by Freepik from Flaticon") // https://www.flaticon.com/packs/weather-255
            Text("designed by Baianat from Flaticon") // https://www.flaticon.com/packs/color-emotions-assets
        }
    }
}
