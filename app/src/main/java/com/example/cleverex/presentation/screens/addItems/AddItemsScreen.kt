package com.example.cleverex.presentation.screens.addItems

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.mlkit.vision.text.Text

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen(
    chosenImage: ImageData?,
    onImageSelect: (Uri) -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Add products") }
            )
        },
        content = {
            AddItemsContent(
                chosenImage = chosenImage,
                onImageSelect = onImageSelect,
                paddingValues = it
            )
        }
    )
}

data class ImageData(
    val imageUri: Uri,
    val extractedText: Text? = null
)