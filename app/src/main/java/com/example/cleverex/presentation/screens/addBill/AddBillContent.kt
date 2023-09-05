package com.example.cleverex.presentation.screens.addBill

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.example.cleverex.presentation.components.ExtractedInformationPicker
import com.example.cleverex.presentation.components.TextRecognitionOverlay
import com.example.cleverex.presentation.screens.categories.CategoriesState
import com.example.cleverex.util.Constants.DATE_AND_TIME_FORMATTER
import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillContent(
    uiState: AddBillViewModel.UiState,
    chosenImageData: AddBillViewModel.ImageData?,
    onImageSelect: (Uri) -> Unit,
    shop: String,
    onShopChanged: (String) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    price: String,
    onPriceChanged: (String) -> Unit,
    billDate: (RealmInstant?),
    onDateChanged: (String) -> Unit,
    paddingValues: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    productPrice: String,
    onProductPriceChange: (String) -> Unit,
    quantityTimesPrice: String,
    onQuantityTimesPriceChange: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onAddItemClicked: () -> Unit,
    unparsedValues: (String),
    onUnparsedValuesChanged: (String) -> Unit,
    categories: State<CategoriesState>
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
    var isNameFieldFocused by remember { mutableStateOf(false) }
    var isQuantityFieldFocused by remember { mutableStateOf(false) }
    var isProductPriceFieldFocused by remember { mutableStateOf(false) }
    var isQuantityTimesPriceFieldFocused by remember { mutableStateOf(false) }
    var isUnparsedValuesFocused by remember { mutableStateOf(false) }


    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onImageSelect(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
//            .padding(bottom = paddingValues.calculateBottomPadding()) removed after adding imePadding() and navigationBarsPadding()
            .padding(bottom = 8.dp)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()
        ) {
//            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())

//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
            ) {
                if (
//                    uiState.billImage.isNotEmpty() &&
                    chosenImageData != null) {
                    TextRecognitionOverlay(
                        chosenImage = chosenImageData, clickedText = { clickedText ->
                            val formattedDate = extractAndFormatDate(clickedText)
                            when {
                                //general bill fields
                                isShopFieldFocused -> onShopChanged(clickedText)
                                isAddressFieldFocused -> onAddressChanged(clickedText)
                                isPriceFieldFocused -> onPriceChanged(
                                    getValidatedDecimal(
                                        clickedText
                                    )
                                )
//                                isDateFieldFocused ->

                                //product fields
                                isDateFieldFocused -> onDateChanged(formattedDate)
                                isNameFieldFocused -> onNameChange(clickedText)
                                isQuantityFieldFocused -> onQuantityChange(clickedText)
                                isProductPriceFieldFocused -> onProductPriceChange(clickedText)
                                isQuantityTimesPriceFieldFocused -> onQuantityTimesPriceChange(
                                    clickedText
                                )

                                isUnparsedValuesFocused -> onUnparsedValuesChanged(clickedText)

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
            Row {
                TextField(
                    value = shop,
                    onValueChange = onShopChanged,
                    placeholder = { Text(text = "Shop name") },
                    modifier = Modifier
                        .weight(1f)
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
                        .weight(1f)
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
            }
            Row {
                TextField(
                    value = if (uiState.price == 0.0) "" else price,
                    onValueChange = {
                        val validatedText = getValidatedDecimal(it)
                        onPriceChanged(
                            (validatedText.ifEmpty { 0.0 }) as String
                        )
                    },
                    placeholder = { Text(text = "Total price") },
                    modifier = Modifier
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged { focusState ->
                            isPriceFieldFocused = focusState.hasFocus
                        }
                        .weight(1f),
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

                var formattedDate = ""
                if (billDate != null) {
                    val zonedDateTime = ZonedDateTime.ofInstant(
                        Instant.ofEpochSecond(
                            billDate.epochSeconds,
                            billDate.nanosecondsOfSecond.toLong()
                        ),
                        ZoneId.systemDefault()
                    )
                    val formatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMATTER)
                    formattedDate = zonedDateTime.format(formatter)
                }
                TextField(
                    value = formattedDate,
                    onValueChange = onDateChanged,
                    placeholder = { Text(text = "Shopping date") },
                    modifier = Modifier
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged { focusState ->
                            isDateFieldFocused = focusState.hasFocus
                        }
                        .weight(1f),
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
        }
        ExtractedInformationPicker(
            onAddItemClicked = { onAddItemClicked() },
            name = name,
            onNameChanged = onNameChange,
            quantity = quantity,
            onQuantityChanged = onQuantityChange,
            price = productPrice,
            onPriceChanged = onProductPriceChange,
            quantityTimesPrice = quantityTimesPrice,
            onQuantityTimesPriceChanged = onQuantityTimesPriceChange,
            nameFocused = { isFocused -> isNameFieldFocused = isFocused },
            quantityFocused = { isFocused -> isQuantityFieldFocused = isFocused },
            priceFocused = { isFocused -> isProductPriceFieldFocused = isFocused },
            quantityTimesPriceFocused = { isFocused ->
                isQuantityTimesPriceFieldFocused = isFocused
            },
            unparsedValues = unparsedValues,
            onUnparsedValuesChanged = onUnparsedValuesChanged,
            unparsedValuesFocused = { isFocused -> isUnparsedValuesFocused = isFocused },
            categories = categories
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val billItems = uiState.billItems.reversed()
            items(billItems.size) { index ->
                val item = billItems[index]
                Text(
                    text = "${item.name} ${item.quantity} ${item.price} ${item.totalPrice}",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                modifier = Modifier
                    .weight(2f),
                onClick = {
                    onSaveClicked()
                },
                shape = Shapes().small
            ) {
                Text(text = "Save", fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
        }

    }
}

fun extractAndFormatDate(input: String): String {
    val pattern = Pattern.compile("""(\d{2}-\d{2}-\d{4} \d{2}-\d{2})""")
    val matcher = pattern.matcher(input)

    if (matcher.find()) {
        val extractedDate = matcher.group(1).replace("-", ":")

        val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM EEEE HH:mm")

        val dateTime =
            ZonedDateTime.parse(extractedDate, inputFormatter.withZone(ZoneId.systemDefault()))
        return dateTime.format(outputFormatter)
    }
    return ""
}

fun formatToDesiredPattern(input: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val outputFormatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMATTER)

    val dateTime = LocalDateTime.parse(input, inputFormatter)
    return dateTime.format(outputFormatter)
}

//fun formatToDesiredPattern(textDate: String): String {
//    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//    val outputFormatter = DateTimeFormatter.ofPattern(DATE_AND_TIME_FORMATTER)
//
//
//    val dateTime = ZonedDateTime.parse(
//        "$textDate${ZoneId.systemDefault()}",
//        inputFormatter.withZone(ZoneId.systemDefault())
//    )
//    return dateTime.format(outputFormatter)
//}

public fun getValidatedDecimal(text: String): String {
    if (text.isEmpty()) return text

    val normalizedText = text.replace(',', '.')
    val filteredChars = normalizedText.filterIndexed { index, c ->
        c.isDigit()
                || (c == '.' && index != 0 && normalizedText.indexOf('.') == index)
                || (c == '.' && index != 0 && normalizedText.count { it == '.' } <= 1)

    }

    // If dot is present, take digits before decimal and first 2 digits after decimal
    return if (filteredChars.count { it == '.' } == 1) {
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        beforeDecimal + "." + afterDecimal.take(2)
    }
    // If there is no dot, return the digits
    else {
        filteredChars
    }
}
