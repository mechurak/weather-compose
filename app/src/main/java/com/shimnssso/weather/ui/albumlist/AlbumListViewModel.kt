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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shimnssso.weather.firebase.MyFirebase
import com.shimnssso.weather.viewmodels.AlbumDigest
import kotlinx.coroutines.launch

class AlbumListViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    private var _currentUserId = MutableLiveData<String>("")
    val currentUserId: LiveData<String>
        get() = _currentUserId

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _albumDigests: MutableLiveData<List<AlbumDigest>> = MutableLiveData(mutableListOf())
    val albumDigests: LiveData<List<AlbumDigest>>
        get() = _albumDigests

    init {
        _currentUserId.value = Firebase.auth.currentUser?.uid ?: ""
        fetchAlbumList()
    }

    fun fetchAlbumList() {
        _isLoading.value = true
        viewModelScope.launch {
            _albumDigests.value = MyFirebase.getAlbumDigests()
            _isLoading.value = false
        }
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumListViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
