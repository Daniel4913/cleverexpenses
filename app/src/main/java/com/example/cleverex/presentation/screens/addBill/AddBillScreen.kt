package com.example.cleverex.presentation.screens.addBill

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.cleverex.displayable.category.CategoryDisplayable
import org.mongodb.kbson.ObjectId
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
    categories: List<CategoryDisplayable>,
    onCategoryClicked: (ObjectId, Boolean) -> Unit
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
        content = { paddingValues ->
            AddBillContent(
                uiState = uiState,
                shop = uiState.shop,
                onShopChanged = onShopChanged,
                address = uiState.address,
                onAddressChanged = onAddressChanged,
                price = uiState.price.toString(),
                onPriceChanged = onPriceChanged,
                paddingValues = paddingValues,
                onSaveClicked = onSaveClicked,
                onAddItemClicked = onAddItemClicked,
                chosenImageData = chosenImageData,
                onImageSelect = onImageSelect,
                billDate = uiState.updatedDateAndTime,
                onDateChanged = { it.toString() }, // todo This needs to be updated based on how you want to handle it
                productName = uiState.name,
                onProductNameChanged = onNameChanged,
                onQuantityChange = onQuantityChanged,
                onProductPriceChange = onProductPriceChanged,
                onQuantityTimesPriceChange = onQuantityTimesPriceChanged,
                unparsedValues = uiState.unparsedValues,
                onUnparsedValuesChanged = onUnparsedValuesChanged,
                categories = categories,
                onCategoryClicked = { categoryId, picked ->
                    onCategoryClicked(categoryId, picked)
                }
            )
        }
    )
}