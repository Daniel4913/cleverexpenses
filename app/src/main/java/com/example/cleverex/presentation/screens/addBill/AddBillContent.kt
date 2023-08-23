package com.example.cleverex.presentation.screens.addBill

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cleverex.R
import com.example.cleverex.presentation.components.TextRecognitionOverlay
import com.example.cleverex.presentation.screens.addItems.ImageData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillContent(
    uiState: UiState,
    chosenImageData: ImageData?,
    onImageSelect: (Uri) -> Unit,
    shop: String,
    onShopChanged: (String) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    price: String,
    onPriceChanged: (String) -> Unit,
    billDate: (String),
    onDateChanged: (String) -> Unit,
    paddingValues: PaddingValues,
    onSaveClicked: () -> Unit,
    onAddItemsClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    var isShopFieldFocused by remember { mutableStateOf(false) }
    var isAddressFieldFocused by remember { mutableStateOf(false) }
    var isPriceFieldFocused by remember { mutableStateOf(false) }
    var isDateFieldFocused by remember { mutableStateOf(false) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onImageSelect(uri)
            }
        }
    )

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
//            .padding(bottom = paddingValues.calculateBottomPadding()) removed after adding imePadding() and navigationBarsPadding()
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(Color.Green)
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
            ) {
                if (
//                    uiState.billImage.isNotEmpty() &&
                    chosenImageData != null) {
                    TextRecognitionOverlay(
                        chosenImage = chosenImageData, clickedText = { clickedText ->
                            when {
                                isShopFieldFocused -> onShopChanged(clickedText)
                                isAddressFieldFocused -> onAddressChanged(clickedText)
                                isPriceFieldFocused -> onPriceChanged(
                                    getValidatedDecimal(
                                        clickedText
                                    )
                                )

                                isDateFieldFocused -> onDateChanged(clickedText)
                            }
                        })
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(50.dp)
                                .fillMaxWidth(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    R.drawable.ic_bill
                                )
                                .build(),
                            contentDescription = "Bill image"
                        )
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "tu chcialem znalezc ikonkre receipt ale nie ma"
                        )
                        IconButton(onClick = {
                            filePicker.launch("image/*")
                        }) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add receipt image button"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                value = shop,
                onValueChange = onShopChanged,
                placeholder = { Text(text = "Shop name") },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isShopFieldFocused = focusState.hasFocus

                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ), keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        scope.launch {
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                        }
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                maxLines = 1,
                singleLine = true
            )
            TextField(
                value = address,
                onValueChange = onAddressChanged,
                placeholder = { Text(text = "Shop address") },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isAddressFieldFocused = focusState.hasFocus
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),

                )
            if (uiState.selectedBillId != null) {
                Text(text = "tu chcialem date wyswietlic, ale nie chce powtarzac kodu z topbara :E")
            }

            TextField(
                value = if (uiState.price == 0.0) "" else price,
                onValueChange = { onPriceChanged(getValidatedDecimal(it)) },
                placeholder = { Text(text = "Total price") },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isPriceFieldFocused = focusState.hasFocus
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
            TextField(
                value = billDate,
                onValueChange = onDateChanged,
                placeholder = { Text(text = "Shopping date") },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isDateFieldFocused = focusState.hasFocus
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
            )
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    onClick = {
                        onSaveClicked()
                        //                    if (uiState.shop.isNotEmpty()) {
                        //                        onSaveClicked(
                        //                            Bill().apply {
                        //                                // we need this. to not refer to Bill specified in fun BillContent(bill:Bill)
                        //                                this.shop = uiState.shop
                        //                                this.address = uiState.address
                        //                                this.price = uiState.price
                        ////                                this.billDate = uiState.billDate STRING
                        //                            }
                        //                        )
                        //                    } else {
                        //                        Toast.makeText(
                        //                            context,
                        //                            "Shop name is required",
                        //                            Toast.LENGTH_SHORT
                        //                        ).show()
                        //                    }
                    },
                    shape = Shapes().small
                ) {
                    Text(text = "Save")
                }
                Button(
                    modifier = Modifier
                        .weight(2f)
                        .height(54.dp),
                    onClick =
                    onAddItemsClicked,
                    shape = Shapes().small
                ) {
                    Text(text = "Add Items")
                }
            }
        }
    }
}

private fun getValidatedDecimal(text: String): String {
    val filteredChars = text.filterIndexed { index, c ->
        c.isDigit()
                || (c == '.' && index != 0 && text.indexOf('.') == index)
                || (c == '.' && index != 0 && text.count { it == '.' } <= 1)
    }
    // If dot is present, take first 3 digits before decimal and first 2 digits after decimal
    return if (filteredChars.count { it == '.' } == 1) {
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        beforeDecimal.take(10) + "." + afterDecimal.take(2)
    }
    // If there is no dot, just take the first 3 digits
    else {
        filteredChars.take(10)
    }
}
