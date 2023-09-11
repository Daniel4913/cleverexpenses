package com.example.cleverex.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.presentation.screens.categories.CategoryOverview
import org.mongodb.kbson.ObjectId

@Composable
fun ProductPicker(
    modifier: Modifier,
    onAddItemClicked: () -> Unit,
    productName: String,
    onProductNameChanged: (String) -> Unit,
    productNameFocused: (Boolean) -> Unit,
    unparsedValues: (String),
    onUnparsedValuesChanged: (String) -> Unit,
    unparsedValuesFocused: (Boolean) -> Unit,
    allCategories: List<CategoryDisplayable>,
    onCategoryClicked: (ObjectId, Boolean) -> Unit,
    ) {
    val focusRequester = FocusRequester()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OcrTextField(
                modifier = Modifier.fillMaxWidth(),
                value = productName,
                onValueChange = onProductNameChanged,
                placeholderText = "Product name",
                focusRequester = focusRequester,
                onFocusChanged = { productNameFocused(it) }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OcrTextField(
                modifier = Modifier.weight(3f),
                value = unparsedValues,
                onValueChange = onUnparsedValuesChanged,
                placeholderText = "ilosc cena cena",
                focusRequester = focusRequester,
                onFocusChanged = { unparsedValuesFocused(it) }
            )
            IconButton(
                onClick = {
                    onAddItemClicked()
                },
            ) {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Add item button")
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allCategories) { categoryDisplayable ->
                CategoryOverview(
                    id = categoryDisplayable.id,
                    name = categoryDisplayable.name,
                    icon = categoryDisplayable.icon,
                    color = categoryDisplayable.categoryColor,
                    onClick = { id, picked ->
                        onCategoryClicked(id, picked)
                    },
                    categoryPicked = categoryDisplayable.categoryPicked
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}




