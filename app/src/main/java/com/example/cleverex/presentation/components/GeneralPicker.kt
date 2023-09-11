package com.example.cleverex.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GeneralPicker(
    modifier: Modifier,
    shop: String,
    onShopChanged: (String) -> Unit,
    shopFieldFocused: (Boolean) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    addressFieldFocused: (Boolean) -> Unit,
    price: String,
    onPriceChanged: (String) -> Unit,
    priceFieldFocused: (Boolean) -> Unit,
    formattedDate: String,
    onDateChanged: (String) -> Unit,
    dateFieldFocused: (Boolean) -> Unit
) {
    val focusRequester = FocusRequester()
    var isShopFieldFocused by remember { mutableStateOf(false) }
    var isAddressFieldFocused by remember { mutableStateOf(false) }
    var isPriceFieldFocused by remember { mutableStateOf(false) }
    var isDateFieldFocused by remember { mutableStateOf(false) }

        Row() {
            OcrTextField(
                modifier = Modifier.weight(1f),
                value = shop,
                onValueChange = onShopChanged,
                placeholderText = "Shop name",
//                isFocused = isShopFieldFocused,
                onFocusChanged = { shopFieldFocused(it) },
                focusRequester = focusRequester,
            )
            OcrTextField(
                modifier = Modifier.weight(1f),
                value = address,
                onValueChange = onAddressChanged,
                placeholderText = "address",
//                isFocused = isAddressFieldFocused,
                onFocusChanged = { addressFieldFocused(it) },
                focusRequester = focusRequester
            )
        }
        Row() {
            OcrTextField(
                modifier = Modifier.weight(1f),
                value = price,
                onValueChange = onPriceChanged,
                placeholderText = "total price",
//                isFocused = isPriceFieldFocused,
                onFocusChanged = { priceFieldFocused(it) },
                focusRequester = focusRequester,
            )
            OcrTextField(
                modifier = Modifier.weight(1f),
                value = formattedDate,
                onValueChange = onDateChanged,
                placeholderText = "Date",
//                isFocused = isDateFieldFocused,
                onFocusChanged = { dateFieldFocused(it) },
                focusRequester = focusRequester,
            )
        }
}