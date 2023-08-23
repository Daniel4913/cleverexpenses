package com.example.cleverex.presentation.screens.addItems

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cleverex.presentation.components.ExtractedInformationPicker
import com.example.cleverex.presentation.components.TextRecognitionOverlay

@Composable
fun AddItemsContent(
    chosenImage: ImageData?,
    onImageSelect: (Uri) -> Unit,
    paddingValues: PaddingValues
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantityTimesPrice by remember { mutableStateOf("") }
    var unitOfMeasure = ""

    var isNameFieldFocused by remember { mutableStateOf(false) }
    var isQuantityFieldFocused by remember { mutableStateOf(false) }
    var isPriceFieldFocused by remember { mutableStateOf(false) }
    var isQuantityTimesPriceFieldFocused by remember { mutableStateOf(false) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onImageSelect(uri)
            }
        }
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .height(300.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (chosenImage != null) {
                TextRecognitionOverlay(
                    chosenImage = chosenImage,
                    clickedText = { clickedText ->
                        when {
                            isNameFieldFocused -> name = clickedText
                            isQuantityFieldFocused -> quantity = clickedText
                            isPriceFieldFocused -> price = clickedText
                            isQuantityTimesPriceFieldFocused -> quantityTimesPrice =
                                clickedText
                        }
                    })
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    IconButton(onClick = {
                        filePicker //TODO
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Chose receipt image"
                        )
                    }
                }
            }

        }
        ExtractedInformationPicker(
            onClick = { /*TODO*/ },
            name = name,
            onNameChanged = {},
            quantity = quantity,
            onQuantityChanged = {},
            price = price,
            onPriceChanged = {},
            quantityTimesPrice = quantityTimesPrice,
            onQuantityTimesPriceChanged = {},
            nameFocused = { isFocused -> isNameFieldFocused = isFocused },
            quantityFocused = { isFocused -> isQuantityFieldFocused = isFocused },
            priceFocused = { isFocused -> isPriceFieldFocused = isFocused },
            quantityTimesPriceFocused = { isFocused ->
                isQuantityTimesPriceFieldFocused = isFocused
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val itemsCount = 50
            items(itemsCount) { index ->
                Text(
                    text = "Element $index",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }
        }
    }
}

