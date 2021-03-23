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
package com.shimnssso.weather.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shimnssso.weather.R

private val Jua = FontFamily(
    Font(R.font.jua_regular, FontWeight.W400),
)

// Set of Material typography styles to start with
val typography = Typography(
    h1 = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
    ),
    h2 = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
    ),
    h3 = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp,
    ),
    body1 = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    button = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        letterSpacing = 1.15.sp
    ),
    caption = TextStyle(
        fontFamily = Jua,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
    )
)
