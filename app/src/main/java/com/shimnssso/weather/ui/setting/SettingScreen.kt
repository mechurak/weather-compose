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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
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
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.firebase.ui.auth.AuthUI
import com.shimnssso.weather.MainActivity
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.database.WeatherDatabase
import com.shimnssso.weather.viewmodels.AssetViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import java.io.File

@Composable
fun SettingScreen(
    navController: NavController? = null,
) {
    val activity = LocalContext.current as MainActivity

    val dataSource = WeatherDatabase.getInstance(activity).photoDao
    val assetViewModel: AssetViewModel = viewModel(
        factory = AssetViewModel.Factory(dataSource, activity.application)
    )

    val currentUserId by assetViewModel.currentUserId.observeAsState("")
    val isLoading by assetViewModel.isLoading.observeAsState(false)

    val showUploadDialog = remember { mutableStateOf(false) }

    Column {
        Spacer(
            Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )
        Scaffold(
            topBar = { AppBar(navController, currentUserId) { showUploadDialog.value = true } },
            modifier = Modifier.navigationBarsPadding()
        ) {
            val sunnyPhotos by assetViewModel.sunnyPhotos.observeAsState(listOf())
            val cloudyPhotos by assetViewModel.cloudyPhotos.observeAsState(listOf())
            val rainyPhotos by assetViewModel.rainyPhotos.observeAsState(listOf())
            val snowyPhotos by assetViewModel.snowyPhotos.observeAsState(listOf())

            val airVeryGoodPhotos by assetViewModel.airVeryGoodPhotos.observeAsState(listOf())
            val airFairPhotos by assetViewModel.airFairPhotos.observeAsState(listOf())
            val airModeratePhotos by assetViewModel.airModeratePhotos.observeAsState(listOf())
            val airPoorPhotos by assetViewModel.airPoorPhotos.observeAsState(listOf())
            val airVeryPoorPhotos by assetViewModel.airVeryPoorPhotos.observeAsState(listOf())

            val currentPhoto = remember { mutableStateOf<Photo?>(null) }
            val openDialog = remember { mutableStateOf(false) }

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Header(
                    title = "Weather",
                    description = "(designed by Freepik from Flaticon)"
                ) // https://www.flaticon.com/packs/weather-255

                CategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_1_SUNNY,
                    photos = sunnyPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_2_CLOUDY,
                    photos = cloudyPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_3_RAINY,
                    photos = rainyPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_WEATHER_4_SNOWY,
                    photos = snowyPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )

                Header(
                    title = "Air Pollution",
                    description = "(designed by Baianat from Flaticon)"
                ) // https://www.flaticon.com/packs/color-emotions-assets                Text("designed by Baianat from Flaticon")

                CategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_1_VERY_GOOD,
                    photos = airVeryGoodPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_2_FAIR,
                    photos = airFairPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_3_MODERATE,
                    photos = airModeratePhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_4_POOR,
                    photos = airPoorPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
                Divider()

                CategoryItem(
                    category = AssetViewModel.CATEGORY_AIR_5_VERY_POOR,
                    photos = airVeryPoorPhotos,
                    onFocus = { assetViewModel.changeCurCategory(it) },
                    onLongPressed = {
                        currentPhoto.value = it
                        openDialog.value = true
                    }
                )
            }

            if (openDialog.value) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CoilImage(
                        data = File(currentPhoto.value!!.path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(Modifier.matchParentSize()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        },
                        fadeIn = true,
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(5.dp, Color.Black, CircleShape)
                            .align(Alignment.Center)
                    )
                }

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    // title = {
                    //     Text(text = "Remove the image")
                    // },
                    text = {
                        Text("Are you sure to remove the image from '${currentPhoto.value!!.category}' category?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                assetViewModel.removeImage(currentPhoto.value!!)
                                openDialog.value = false
                            }
                        ) {
                            Text("Remove")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showUploadDialog.value) {
                UploadDialog(
                    onDismiss = {
                        showUploadDialog.value = false
                    },
                    onConfirm = { title ->
                        showUploadDialog.value = false
                        assetViewModel.uploadAlbum(title)
                    }
                )
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun AppBar(
    navController: NavController?,
    currentUserID: String,
    onUploadBtn: () -> Unit
) {
    val activity = LocalContext.current as MainActivity
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController!!.navigate("home") }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            if (currentUserID.isEmpty()) {
                IconButton(onClick = {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                    activity.startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                        MainActivity.SIGN_REQUEST
                    )
                }) {
                    Icon(Icons.Filled.Login, contentDescription = null)
                }
            } else {
                IconButton(onClick = { /* TODO: Navigate to online album screen */ }) {
                    Icon(Icons.Filled.GetApp, contentDescription = null)
                }

                IconButton(onClick = {
                    onUploadBtn()
                }) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                }

                IconButton(onClick = {
                    AuthUI.getInstance().signOut(activity)
                }) {
                    Icon(Icons.Filled.Logout, contentDescription = null)
                }
            }
        },
        title = {
            Text(text = "Settings")
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}
