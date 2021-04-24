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
package com.shimnssso.weather.firebase

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shimnssso.weather.database.Photo
import com.shimnssso.weather.viewmodels.Album
import com.shimnssso.weather.viewmodels.AlbumDigest
import com.shimnssso.weather.viewmodels.AssetViewModel
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object MyFirebase {
    private const val COLLECTION_ALBUMS = "albums"
    private const val COLLECTION_ALBUM_DIGESTS = "albumDigests"

    private suspend fun uploadAlbumDoc(
        title: String,
        docId: String,
        sunnyPhotos: ArrayList<String>,
        cloudyPhotos: ArrayList<String>,
        rainyPhotos: ArrayList<String>,
        snowyPhotos: ArrayList<String>,
        airVeryGoodPhotos: ArrayList<String>,
        airFairPhotos: ArrayList<String>,
        airModeratePhotos: ArrayList<String>,
        airPoorPhotos: ArrayList<String>,
        airVeryPoorPhotos: ArrayList<String>,
    ) {
        val album = Album(
            sunnyPhotos,
            cloudyPhotos,
            rainyPhotos,
            snowyPhotos,
            airVeryGoodPhotos,
            airFairPhotos,
            airModeratePhotos,
            airPoorPhotos,
            airVeryPoorPhotos,
        )
        try {
            Firebase.firestore.collection(COLLECTION_ALBUMS)
                .document(docId)
                .set(album, SetOptions.merge())
                .await()

            Timber.i("set album. succeeded")

            val albumDigest = AlbumDigest(
                title,
                Firebase.auth.currentUser!!.email!!,
                docId,
                sunnyPhotos[0],
                album.getImageCount()
            )
            Firebase.firestore.collection(COLLECTION_ALBUM_DIGESTS)
                .document(docId)
                .set(albumDigest, SetOptions.merge())
                .await()
            Timber.i("set albumDigest. succeeded")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun uploadAlbum(title: String, photos: List<Photo>): Boolean {
        if (Firebase.auth.currentUser == null) {
            Timber.e("currentUser is null!!")
            return false
        }

        val doc = Firebase.firestore.collection(COLLECTION_ALBUMS).document()
        val docId = doc.id

        val sunnyPhotos = ArrayList<String>()
        val cloudyPhotos = ArrayList<String>()
        val rainyPhotos = ArrayList<String>()
        val snowyPhotos = ArrayList<String>()

        val airVeryGoodPhotos = ArrayList<String>()
        val airFairPhotos = ArrayList<String>()
        val airModeratePhotos = ArrayList<String>()
        val airPoorPhotos = ArrayList<String>()
        val airVeryPoorPhotos = ArrayList<String>()

        val storageFolder = Firebase.storage.getReference(docId)
        for (photo in photos) {
            val uri = Uri.parse("file://" + photo.path)
            val ref = storageFolder.child(uri.lastPathSegment!!)
            try {
                val taskSnapshot = ref.putFile(uri).await()
                val downloadUrl = taskSnapshot.metadata!!.reference!!.downloadUrl.await()
                val targetList = when (photo.category) {
                    AssetViewModel.CATEGORY_WEATHER_1_SUNNY -> sunnyPhotos
                    AssetViewModel.CATEGORY_WEATHER_2_CLOUDY -> cloudyPhotos
                    AssetViewModel.CATEGORY_WEATHER_3_RAINY -> rainyPhotos
                    AssetViewModel.CATEGORY_WEATHER_4_SNOWY -> snowyPhotos
                    AssetViewModel.CATEGORY_AIR_1_VERY_GOOD -> airVeryGoodPhotos
                    AssetViewModel.CATEGORY_AIR_2_FAIR -> airFairPhotos
                    AssetViewModel.CATEGORY_AIR_3_MODERATE -> airModeratePhotos
                    AssetViewModel.CATEGORY_AIR_4_POOR -> airPoorPhotos
                    AssetViewModel.CATEGORY_AIR_5_VERY_POOR -> airVeryPoorPhotos
                    else -> sunnyPhotos
                }
                targetList.add(downloadUrl.toString())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        uploadAlbumDoc(
            title, docId,
            sunnyPhotos,
            cloudyPhotos,
            rainyPhotos,
            snowyPhotos,
            airVeryGoodPhotos,
            airFairPhotos,
            airModeratePhotos,
            airPoorPhotos,
            airVeryPoorPhotos,
        )
        return true
    }

    suspend fun getAlbumDigests(): ArrayList<AlbumDigest> {
        val retList: ArrayList<AlbumDigest> = ArrayList()
        try {
            val querySnapshot = Firebase.firestore.collection(COLLECTION_ALBUM_DIGESTS)
                .get()
                .await()

            Timber.i("getAlbumList(). succeeded")
            for (document in querySnapshot.documents) {
                val albumDigest = document.toObject(AlbumDigest::class.java)!!
                retList.add(albumDigest)
            }
        } catch (e: Exception) {
        }
        return retList
    }

    suspend fun getAlbum(docId: String): Album? {
        try {
            val documentSnapshot = Firebase.firestore.collection(COLLECTION_ALBUMS)
                .document(docId)
                .get()
                .await()

            Timber.i("getAlbum(). succeeded")
            return documentSnapshot.toObject(Album::class.java)
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }
}
