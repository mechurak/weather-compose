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
package com.shimnssso.weather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(night: Photo)

    @Update
    suspend fun update(night: Photo)

    // Room already uses a background thread for that specific @Query which returns LiveData.
    @Query("SELECT * from photo_table WHERE category = :category")
    fun getPhotos(category: String): LiveData<List<Photo>>

    @Query("DELETE FROM photo_table WHERE photoId = :key")
    suspend fun remove(key: Long)
}