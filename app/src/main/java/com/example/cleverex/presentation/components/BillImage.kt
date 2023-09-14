package com.example.cleverex.presentation.components

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun BillImage(
    image: Uri
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "Gallery Image"
        )
    }
}