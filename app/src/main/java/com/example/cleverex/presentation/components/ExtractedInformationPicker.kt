package com.example.cleverex.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.presentation.screens.categories.CategoriesState
import com.example.cleverex.presentation.screens.categories.CategoryOverview
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ExtractedInformationPicker(
    onAddItemClicked: () -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    nameFocused: (Boolean) -> Unit,
    unparsedValues: (String),
    onUnparsedValuesChanged: (String) -> Unit,
    unparsedValuesFocused: (Boolean) -> Unit,
    allCategories: List<CategoryDisplayable>,
    onCategoryClicked: (ObjectId, Boolean) -> Unit,

    ) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusRequester = FocusRequester()
    var isNameFocused by remember { mutableStateOf(false) }
    var isUnparsedValuesFocused by remember { mutableStateOf(false) }
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




