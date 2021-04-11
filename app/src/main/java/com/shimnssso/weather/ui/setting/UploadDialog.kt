package com.shimnssso.weather.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun UploadDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Text(
                text = "Upload Album",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Title") },
                modifier = Modifier.padding(8.dp)
            )
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                onClick = { onConfirm(text) }
            ) {
                Text("Upload")
            }
        }
    }
}