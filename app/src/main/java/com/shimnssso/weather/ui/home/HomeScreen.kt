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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.shimnssso.weather.R

@Composable
fun HomeScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    navController: NavController? = null
) {
    Scaffold(
        topBar = { AppBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Suji-gu")
            Text("Fri, 19 Mar 2021, 8:00PM")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.size(200.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ella),
                        contentDescription = "ella",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape) // clip to the circle shape
                            .border(2.dp, Color.Gray, CircleShape) // add a border (optional)
                    )
                    Icon(
                        Icons.Filled.WbSunny,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .size(30.dp)
                            .background(Color.Green.copy(alpha = 0.7f))
                            .align(Alignment.BottomEnd)
                    )
                }
                Column {
                    Text("16º")
                    Text("18º/3º")
                    Text("Good")
                }
            }

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
                        Column {
                            Text("12AM")
                            Icon(
                                Icons.Filled.WbSunny,
                                contentDescription = null,
                            )
                            Text("16º")
                            Text("30%")
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
                        Text("80%")
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
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
                            Text("80%")
                            Icon(
                                Icons.Filled.WbSunny,
                                contentDescription = null,
                            )
                            Text("18º/2º")
                        }
                    }
                }
                Divider(Modifier.padding(vertical = 12.dp))
                Text("designed by Freepik from Flaticon") // https://www.flaticon.com/packs/weather-255
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
