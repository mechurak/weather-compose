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

data class Album(
    val name: String = "",
    val userId: String = "",
    var documentId: String = "",
    val sunnyImages: ArrayList<String> = ArrayList(),
    val image: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val likeCount: Int = -1,
    val downloadCount: Int = -1,
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
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!, // weather
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
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
        parcel.writeString(name)
        parcel.writeString(userId)
        parcel.writeString(documentId)
        parcel.writeStringList(sunnyImages)
        parcel.writeString(image)
        parcel.writeLong(timeStamp)
        parcel.writeInt(likeCount)
        parcel.writeInt(downloadCount)
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

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}
