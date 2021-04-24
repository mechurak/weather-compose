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

    init {
        _isLoading.value = true
        viewModelScope.launch {
            _photos.value = MyFirebase.getAlbum(docId)
            _isLoading.value = false
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