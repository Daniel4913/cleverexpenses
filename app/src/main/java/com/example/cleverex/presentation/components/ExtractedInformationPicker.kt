package com.example.cleverex.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExtractedInformationPicker(
    onAddItemClicked: () -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    quantity: (String),
    onQuantityChanged: (String) -> Unit,
    price: String,
    onPriceChanged: (String) -> Unit,
    quantityTimesPrice: (String),
    onQuantityTimesPriceChanged: (String) -> Unit,
    nameFocused: (Boolean) -> Unit,
    quantityFocused: (Boolean) -> Unit,
    priceFocused: (Boolean) -> Unit,
    quantityTimesPriceFocused: (Boolean) -> Unit,
    unparsedValues: (String),
    onUnparsedValuesChanged: (String) -> Unit,
    unparsedValuesFocused: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isNameFocused by remember { mutableStateOf(false) }
    var isQuantityFocused by remember { mutableStateOf(false) }
    var isPriceFocused by remember { mutableStateOf(false) }
    var isQuantityTimesPriceFocused by remember { mutableStateOf(false) }
    var isUnparsedValuesFocused by remember { mutableStateOf(false) }

    var clickCount by remember { mutableStateOf(0) }

    val keyboardController = LocalSoftwareKeyboardController.current
    LocalSoftwareKeyboardController.current




    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                modifier = Modifier
                    .weight(3f)
                    .focusRequester(focusRequester)
                    .clickable {
                        clickCount++
                        if (clickCount >= 2) {
                            keyboardController?.show()
                            Timber.d("$clickCount")
                        }
                        focusRequester.requestFocus()
                    }
                    .onFocusChanged {
                        isNameFocused = it.hasFocus
                        nameFocused(it.hasFocus)
                        if (it.isFocused) {
                            keyboardController?.hide()
                        } else {
                            keyboardController?.hide() //TODO bo mnie sie podoba wiec tak ma byc, ze jak sie kliknie ikonke to dopiero sie wysuwa
                        }
                    },
                value = name,
                onValueChange = onNameChanged,
                placeholder = { Text(text = "Product name") },
                trailingIcon = {
                    IconButton(onClick = {
                        keyboardController?.show()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "showKeyboard"
                        )
                    }
                },
//                colors = TextFieldDefaults.colors(
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                maxLines = 1,
                singleLine = true,
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(modifier = Modifier
                .weight(2f)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isUnparsedValuesFocused = it.hasFocus
                    unparsedValuesFocused(it.hasFocus)
                    if (it.isFocused) {
                        keyboardController?.hide()
                    } else {
                        keyboardController?.hide() //TODO bo mnie sie podoba wiec tak ma byc, ze jak sie kliknie ikonke to dopiero sie wysuwa
                    }
                },
                value = unparsedValues,
                onValueChange = onUnparsedValuesChanged,
                placeholder = {
                    Text(
                        text = "ilosc cena cena",
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        keyboardController?.show()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "showKeyboard"
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ), keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        scope.launch {
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                        }
                        keyboardController?.hide()
                    }
                ),
                maxLines = 1,
                singleLine = true)



            IconButton(

                onClick = {
                    onAddItemClicked()
                },

                ) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Add item button")
            }
        }
        Row(
//            modifier = Modifier
//                .background(Color.White),
            horizontalArrangement = Arrangement.Center //TODO NIE DZIAUA
        ) {
            Text(text = "categorty hor scrol")
//            DatePicker()
//            DatePicker()
//            DatePicker()
        }
    }
}



