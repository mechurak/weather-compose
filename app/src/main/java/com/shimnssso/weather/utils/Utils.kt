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
package com.shimnssso.weather.utils

import kotlin.math.roundToInt

object Utils {
    private const val KELVIN_CELSIUS_DIFF = 273.15f // 0ºC = 273.15K
    const val ONE_HOUR_IN_SEC = 60 * 60 // 3600
    const val ONE_DAY_IN_SEC = 60 * 60 * 24 //

    fun getTemp(kelvinTemp: Float, isCelsius: Boolean = true): Int {
        // TODO: Support fahrenheit as well

        return (kelvinTemp - KELVIN_CELSIUS_DIFF).roundToInt()
    }
}