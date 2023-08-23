package com.example.cleverex.presentation.screens.addItems

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

