package com.example.cleverex.presentation.screens.addItems

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text

class AddItemsViewModel() : ViewModel() {
    val imageState = ImageState()

    fun addImage(imageUri: Uri){
        imageState.addImage(ImageData(imageUri =imageUri, extractedText = null))
    }
}

class ImageState {
    val image = mutableStateListOf<ImageData>()

    fun addImage(imageData: ImageData){
        image.clear()
        image.add(imageData)
    }
}

data class ImageData(
    val imageUri: Uri,
    val extractedText: Text? = null
)