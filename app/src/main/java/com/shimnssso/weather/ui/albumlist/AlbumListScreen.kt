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
package com.shimnssso.weather.ui.albumlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.shimnssso.weather.MainActivity

@Composable
fun AlbumListScreen(
    navController: NavController? = null,
) {

    val activity = LocalContext.current as MainActivity

    val viewModel: AlbumListViewModel = viewModel(
        factory = AlbumListViewModel.Factory(activity.application)
    )

    val currentUserId by viewModel.currentUserId.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val albums by viewModel.albums.observeAsState(listOf())

    Column {
        Spacer(
            Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .statusBarsHeight() // Match the height of the status bar
                .fillMaxWidth()
        )
        Scaffold(
            topBar = { AppBar(navController, currentUserId) },
            modifier = Modifier.navigationBarsPadding()
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(albums) { album ->
                    AlbumItem(album)
                }
            }
        }
    }
}

@Composable
private fun AppBar(
    navController: NavController?,
    currentUserID: String
) {
    val activity = LocalContext.current as MainActivity
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController!!.navigate("home") }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = { /* TODO: */ }
            ) {
                Icon(Icons.Filled.Login, contentDescription = null)
            }
        },
        title = {
            Text(text = "Album List")
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}
