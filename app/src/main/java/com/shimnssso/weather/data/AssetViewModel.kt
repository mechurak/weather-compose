package com.shimnssso.weather.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AssetViewModel : ViewModel() {
    var savedImagePath: String? by mutableStateOf(null)
        private set

    fun onImageSaved(imagePath: String) {
        savedImagePath = imagePath
    }
}