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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shimnssso.weather.firebase.MyFirebase
import com.shimnssso.weather.viewmodels.Album
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val app: Application,
    private val docId: String,
) : AndroidViewModel(app) {

    private var _photos = MutableLiveData<Album>()
    val photos: LiveData<Album>
        get() = _photos

    private var _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _isDownloadDone = MutableLiveData<Boolean>(false)
    val isDownloadDone: LiveData<Boolean>
        get() = _isDownloadDone

    init {
        _isLoading.value = true
        viewModelScope.launch {
            _photos.value = MyFirebase.getAlbum(docId)
            _isLoading.value = false
        }
    }

    fun downloadAlbum() {
        _isLoading.value = true
        viewModelScope.launch {
            // TODO: Remove current album
            // TODO: Download this online album to device
            delay(2000)
            _isLoading.value = false
            _isDownloadDone.value = true
        }
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(
        private val application: Application,
        private val docId: String,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(application, docId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
