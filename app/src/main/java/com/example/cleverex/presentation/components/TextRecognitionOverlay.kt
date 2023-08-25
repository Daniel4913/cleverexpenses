package com.example.cleverex.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.cleverex.mlkit.BitmapUtils
import com.example.cleverex.mlkit.GraphicOverlay
import com.example.cleverex.mlkit.TextRecognitionProcessor
import com.example.cleverex.presentation.screens.addItems.ImageData
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import timber.log.Timber

@Composable
fun TextRecognitionOverlay(
    chosenImage: ImageData,
    clickedText: (String) -> Unit
) {
    val context = LocalContext.current
    val imageProcessor = TextRecognitionProcessor(context, TextRecognizerOptions.Builder().build())
    val graphicOverlay = remember { GraphicOverlay(context) }
    val imageBitmap = remember(chosenImage.imageUri) {
        BitmapUtils.getBitmapFromContentUri(context.contentResolver, chosenImage.imageUri)
    }

    val clickedTextState = remember { mutableStateOf<String?>(null) }
    graphicOverlay.setOnTextClickListener { clickedTextBlock ->
        clickedTextState.value = clickedTextBlock
        clickedText(clickedTextBlock)
    }

    Box(contentAlignment = Alignment.Center) {
        imageBitmap?.let { bitmap ->
            val imageModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Receipt bitmap",
                modifier = imageModifier,
                contentScale = ContentScale.FillWidth
            )

            AndroidView(
                modifier = imageModifier,
                factory = { graphicOverlay }) { viewGraphicOverlay ->
                viewGraphicOverlay.layoutParams.width = bitmap.width
                viewGraphicOverlay.layoutParams.height = bitmap.height
                viewGraphicOverlay.invalidate()
                graphicOverlay.setImageSourceInfo(bitmap.width, bitmap.height, false)
                imageProcessor.processBitmap(
                    bitmap = bitmap,
                    graphicOverlay = graphicOverlay
                )
            }

            DisposableEffect(Unit) {
                graphicOverlay.clear()
                graphicOverlay.setImageSourceInfo(bitmap.width, bitmap.height, false)
                imageProcessor.processBitmap(
                    bitmap = bitmap,
                    graphicOverlay = graphicOverlay
                )
                onDispose {
                    imageProcessor.stop()
                }
            }
        }
    }
}