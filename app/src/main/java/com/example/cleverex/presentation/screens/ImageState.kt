package com.example.cleverex.presentation.screens

import androidx.compose.runtime.mutableStateListOf

class ImageState {
    val image = mutableStateListOf<ImageData>()

    fun addImage(imageData: ImageData) {
        image.clear()
        image.add(imageData)
    }
}