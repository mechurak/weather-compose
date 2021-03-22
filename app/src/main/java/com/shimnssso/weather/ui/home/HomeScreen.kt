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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.R
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.viewmodels.Weather
import com.shimnssso.weather.viewmodels.WeatherViewModel

@Composable
fun HomeScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    navController: NavController? = null
) {
    Scaffold(
        topBar = { AppBar(navController) }
    ) { innerPadding ->
        val activity = LocalContext.current as MainActivity

        val dataSource = WeatherDatabase.getInstance(activity).photoDao
        val weatherViewModel: WeatherViewModel = viewModel(
            factory = WeatherViewModel.Factory(dataSource, activity.application)
        )

        val currentLocation by weatherViewModel.currentLocation.observeAsState("no location")
        val todayWeather by weatherViewModel.todayWeather.observeAsState(Weather())
        val weatherPhotoList by weatherViewModel.weatherPhotoList.observeAsState(listOf())
        val airPhotoList by weatherViewModel.weatherPhotoList.observeAsState(listOf())

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            BriefInfo(location = currentLocation, weather = todayWeather, weatherPhotoList = weatherPhotoList, airPhotoList = airPhotoList)

            Divider(Modifier.padding(top = 16.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("12AM")
                            Image(
                                painter = painterResource(R.drawable.ic__02_cloudy),
                                contentDescription = "cloudy",
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Image(
                                painter = painterResource(R.drawable.air_2_fair_47_happy),
                                contentDescription = "fair",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text("16º")
                        }
                    }
                }

                Divider(Modifier.padding(vertical = 12.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Yesterday", modifier = Modifier.fillMaxWidth(0.3f))
                        Image(
                            painter = painterResource(R.drawable.ic__03_rain),
                            contentDescription = "rain",
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.air_5_very_poor_36_devil),
                            contentDescription = "rain",
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text("18º/2º")
                    }
                    for (i in 1..7) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sat", modifier = Modifier.fillMaxWidth(0.3f))
                            Image(
                                painter = painterResource(R.drawable.ic__01_sunny),
                                contentDescription = "sunny",
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Image(
                                painter = painterResource(R.drawable.air_3_moderate_04_surprised),
                                contentDescription = "moderate",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text("18º/2º")
                        }
                    }
                }
                Divider(Modifier.padding(vertical = 12.dp))
                Text("designed by Freepik from Flaticon") // https://www.flaticon.com/packs/weather-255
                Text("designed by Baianat from Flaticon") // https://www.flaticon.com/packs/color-emotions-assets
            }
        }
    }
}

@Composable
private fun AppBar(navController: NavController?) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController!!.navigate("setting") }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { navController!!.navigate("setting") }) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }
        },
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}
