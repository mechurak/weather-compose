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
package com.shimnssso.weather.viewmodels

import android.os.Parcel
import android.os.Parcelable

data class AlbumDigest(
    val title: String = "",
    val userEmail: String = "",
    val detailDocumentId: String = "",
    val mainImage: String = "",
    val imageCount: Int = -1,
    val timeStamp: Long = System.currentTimeMillis(),
    val likeCount: Int = -1,
    val downloadCount: Int = -1,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(userEmail)
        parcel.writeString(detailDocumentId)
        parcel.writeString(mainImage)
        parcel.writeInt(imageCount)
        parcel.writeLong(timeStamp)
        parcel.writeInt(likeCount)
        parcel.writeInt(downloadCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlbumDigest> {
        override fun createFromParcel(parcel: Parcel): AlbumDigest {
            return AlbumDigest(parcel)
        }

        override fun newArray(size: Int): Array<AlbumDigest?> {
            return arrayOfNulls(size)
        }
    }
}


data class Album(
    val sunnyImages: ArrayList<String> = ArrayList(),
    val cloudyImages: ArrayList<String> = ArrayList(),
    val rainyImages: ArrayList<String> = ArrayList(),
    val snowyImages: ArrayList<String> = ArrayList(),
    val airGoodImages: ArrayList<String> = ArrayList(),
    val airFairImages: ArrayList<String> = ArrayList(),
    val airModerateImages: ArrayList<String> = ArrayList(),
    val airPoorImages: ArrayList<String> = ArrayList(),
    val airVeryPoorImages: ArrayList<String> = ArrayList(),
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!, // weather
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!, // air
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(sunnyImages)
        parcel.writeStringList(cloudyImages)
        parcel.writeStringList(rainyImages)
        parcel.writeStringList(snowyImages)
        parcel.writeStringList(airGoodImages)
        parcel.writeStringList(airFairImages)
        parcel.writeStringList(airModerateImages)
        parcel.writeStringList(airPoorImages)
        parcel.writeStringList(airVeryPoorImages)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getImageCount(): Int {
        return sunnyImages.size +
            cloudyImages.size +
            rainyImages.size +
            snowyImages.size +
            airGoodImages.size +
            airFairImages.size +
            airModerateImages.size +
            airPoorImages.size +
            airVeryPoorImages.size
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}
