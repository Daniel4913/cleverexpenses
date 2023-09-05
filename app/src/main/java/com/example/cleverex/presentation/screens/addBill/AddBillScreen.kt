package com.example.cleverex.presentation.screens.addBill

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cleverex.presentation.screens.categories.CategoriesState
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddBillScreen(
    uiState: AddBillViewModel.UiState,
    onShopChanged: (String) -> Unit,
    onAddressChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onBackPressed: () -> Unit,
    onSaveClicked: () -> Unit,
    onAddItemClicked: () -> Unit,
    chosenImageData: AddBillViewModel.ImageData?,
    onImageSelect: (Uri) -> Unit,
    onNameChanged: (String) -> Unit,
    onQuantityChanged: (String) -> Unit,
    onProductPriceChanged: (String) -> Unit,
    onQuantityTimesPriceChanged: (String) -> Unit,
    onUnparsedValuesChanged: (String) -> Unit,
    categories: State<CategoriesState>
) {
    Scaffold(
        topBar = {
            BillTopBar(
                onBackPressed = onBackPressed,
                selectedBill = uiState.selectedBill,
                onDeleteConfirmed = onDeleteConfirmed,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = {
            AddBillContent(
                uiState = uiState,
                shop = uiState.shop,
                onShopChanged = onShopChanged,
                address = uiState.address,
                onAddressChanged = onAddressChanged,
                price = uiState.price.toString(),
                onPriceChanged = onPriceChanged,
                paddingValues = it, // it is referring to our lambda of our content parameter
                onSaveClicked = onSaveClicked,
                onAddItemClicked = onAddItemClicked,
                chosenImageData = chosenImageData,
                onImageSelect = onImageSelect,
                billDate = uiState.updatedDateAndTime,
                onDateChanged = { it.toString() }, // This needs to be updated based on how you want to handle it
                // Adding the new parameters here
                name = uiState.name,
                onNameChange = onNameChanged,
                quantity = uiState.quantity,
                onQuantityChange = onQuantityChanged,
                productPrice = uiState.productPrice,
                onProductPriceChange = onProductPriceChanged,
                quantityTimesPrice = uiState.quantityTimesPrice,
                onQuantityTimesPriceChange = onQuantityTimesPriceChanged,
                unparsedValues = uiState.unparsedValues,
                onUnparsedValuesChanged = onUnparsedValuesChanged,
                categories = categories
            )
        }
    )
}

@Composable
fun BillImage(
    billImage: String?,
    imageShape: CornerBasedShape = Shapes().small,
    imageSize: Dp = 40.dp
) {
    AsyncImage(
        modifier = Modifier
            .clip(imageShape)
            .size(imageSize),
        model = ImageRequest.Builder(
            context = LocalContext.current
        ).data(billImage).crossfade(true).build(),
        contentDescription = "Bill Image"
    )
}