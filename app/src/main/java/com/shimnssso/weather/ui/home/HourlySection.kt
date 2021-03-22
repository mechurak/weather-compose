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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shimnssso.weather.utils.Utils
import com.shimnssso.weather.viewmodels.AssetViewModel
import com.shimnssso.weather.viewmodels.WeatherHour
import dev.chrisbanes.accompanist.coil.CoilImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HourlySection(weatherList: List<WeatherHour>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sdf = SimpleDateFormat("h a", Locale.ENGLISH) // 12AM
        weatherList.forEach { weatherHour ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val date = Date(weatherHour.dt * 1000)
                Text(sdf.format(date), modifier = Modifier.padding(bottom = 8.dp))
                CoilImage(
                    data = AssetViewModel.getImage(weatherHour.weather),
                    contentDescription = weatherHour.weather,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(50.dp)

                )
                CoilImage(
                    data = AssetViewModel.getImage(weatherHour.air),
                    contentDescription = weatherHour.air,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(30.dp)
                )

                val temperatureValue = Utils.getTemp(weatherHour.temp)
                Text("${temperatureValue}º")
            }
        }
    }
}
