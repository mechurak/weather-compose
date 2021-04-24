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

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.shimnssso.weather.viewmodels.AlbumDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AlbumItem(
    albumDigest: AlbumDigest,
    onClick: (docId: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(albumDigest.detailDocumentId)
            }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Image(
            painter = rememberCoilPainter(
                request = albumDigest.mainImage,
                fadeIn = true
            ),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            contentScale = ContentScale.Crop,
        )
        Column(Modifier.padding(16.dp)) {
            val sdf = SimpleDateFormat("EEE, d MMM HH:mm", Locale.ENGLISH) // Wed, 4 Jul 12:08
            val date = Date(albumDigest.timeStamp * 1000)

            Text(text = "${albumDigest.title} (${albumDigest.imageCount} images)")
            Text(text = sdf.format(date))
            Text(text = "by ${albumDigest.userEmail}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Filled.Download,
                        contentDescription = null
                    )
                }
                Text(text = "${albumDigest.downloadCount}")
                Spacer(Modifier.width(16.dp))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null
                    )
                }
                Text(text = "${albumDigest.downloadCount}")
            }
        }
    }
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp)
    )
}
