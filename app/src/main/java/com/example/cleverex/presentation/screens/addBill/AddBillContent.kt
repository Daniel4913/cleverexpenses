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
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.presentation.components.GeneralPicker
import com.example.cleverex.presentation.components.ProductPicker
import com.example.cleverex.presentation.components.TextRecognitionOverlay
import com.example.cleverex.util.Constants.DATE_AND_TIME_FORMATTER
import io.realm.kotlin.types.RealmInstant
import org.mongodb.kbson.ObjectId
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

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
    onQuantityChange: (String) -> Unit,
    onProductPriceChange: (String) -> Unit,
    onQuantityTimesPriceChange: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onAddItemClicked: () -> Unit,
    unparsedValues: (String),
    onUnparsedValuesChanged: (String) -> Unit,
    categories: List<CategoryDisplayable>,
    onCategoryClicked: (ObjectId, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

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
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 8.dp)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
//                                isDateFieldFocused ->  //TODO

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,

//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.Center) {
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
            }
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
            GeneralPicker(
                modifier = Modifier.fillMaxWidth(),
                shop = shop,
                onShopChanged = onShopChanged,
                shopFieldFocused = { isFocused -> isShopFieldFocused = isFocused },
                address = address,
                onAddressChanged = onAddressChanged,
                addressFieldFocused = { isFocused -> isAddressFieldFocused = isFocused },
                price = if (uiState.price == 0.0) "" else price,
                onPriceChanged = onPriceChanged,
                priceFieldFocused = { isFocused -> isPriceFieldFocused = isFocused },
                formattedDate = formattedDate,
                onDateChanged = onDateChanged,
                dateFieldFocused = { isFocused -> isDateFieldFocused = isFocused }
            )
            ProductPicker(
                modifier = Modifier,
                onAddItemClicked = { onAddItemClicked() },
                productName = name,
                onProductNameChanged = onNameChange,
                productNameFocused = { isFocused -> isNameFieldFocused = isFocused },
                unparsedValues = unparsedValues,
                onUnparsedValuesChanged = onUnparsedValuesChanged,
                unparsedValuesFocused = { isFocused -> isUnparsedValuesFocused = isFocused },
                allCategories = categories,
                onCategoryClicked = { id, picked ->
                    onCategoryClicked(id, picked)
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val billItemsDisplayable = uiState.billItemsDisplayable.reversed()
            items(billItemsDisplayable.size) { index ->
                val billItemDisplayable = billItemsDisplayable[index]
                Text(
                    text = "${billItemDisplayable.name} ${billItemDisplayable.quantity} ${billItemDisplayable.unitPrice} ${billItemDisplayable.totalPrice}",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
                Row() {
                    val icons: List<String> = billItemDisplayable.categories.map {
                        it.icon.value
                    }
                    Text(text = "${icons}")
                }
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
