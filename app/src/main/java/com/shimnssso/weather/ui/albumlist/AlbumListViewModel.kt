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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shimnssso.weather.viewmodels.AlbumDigest
import timber.log.Timber

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
        Firebase.firestore.collection("albumDigests")
            .get()
            .addOnSuccessListener {
                Timber.i("getAlbumList(). succeeded")
                Timber.i("getAlbumList(). $it")
                // Here we have created a new instance for Boards ArrayList.
                val retList: ArrayList<AlbumDigest> = ArrayList()
                for (i in it.documents) {
                    Timber.i("i: $i")
                    val albumDigest = i.toObject(AlbumDigest::class.java)!!
                    retList.add(albumDigest)
                }
                _albumDigests.value = retList
            }
            .addOnFailureListener { e ->
                Timber.i("getAlbumList(). failed. $e")
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
