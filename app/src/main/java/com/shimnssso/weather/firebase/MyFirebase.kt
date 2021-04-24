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
import kotlinx.coroutines.tasks.await
import timber.log.Timber

object MyFirebase {
    private const val COLLECTION_ALBUMS = "albums"
    private const val COLLECTION_ALBUM_DIGESTS = "albumDigests"

    private suspend fun uploadAlbumDoc(title: String, docId: String, uploadedSunnyPhotos: ArrayList<String>) {
        val album = Album(uploadedSunnyPhotos)
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
                uploadedSunnyPhotos[0],
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

        val uploadedSunnyPhotos = ArrayList<String>()
        val storageFolder = Firebase.storage.getReference(docId)
        for (photo in photos) {
            val uri = Uri.parse("file://" + photo.path)
            val ref = storageFolder.child(uri.lastPathSegment!!)
            try {
                val taskSnapshot = ref.putFile(uri).await()
                val downloadUrl = taskSnapshot.metadata!!.reference!!.downloadUrl.await()
                uploadedSunnyPhotos.add(downloadUrl.toString())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        uploadAlbumDoc(title, docId, uploadedSunnyPhotos)
        return true
    }

    suspend fun getAlbumDigests(): ArrayList<AlbumDigest> {
        val retList: ArrayList<AlbumDigest> = ArrayList()
        try {
            val querySnapshot = Firebase.firestore.collection(COLLECTION_ALBUM_DIGESTS)
                .get()
                .await()

            Timber.i("getAlbumList(). succeeded")
            Timber.i("getAlbumList(). $querySnapshot")
            for (document in querySnapshot.documents) {
                Timber.d("document: $document")
                val albumDigest = document.toObject(AlbumDigest::class.java)!!
                retList.add(albumDigest)
            }
        } catch (e: Exception) {
        }
        return retList
    }
}
