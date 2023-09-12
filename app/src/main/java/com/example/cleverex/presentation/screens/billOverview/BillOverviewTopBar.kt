package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleverex.domain.Bill
import com.example.cleverex.presentation.components.DisplayAlertDialog
import com.example.cleverex.util.toInstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillOverviewTopBar(
    selectedBill: Bill?,
    onDeleteConfirmed: () -> Unit,
    onEditPressed: () -> Unit,
    onBackPressed: () -> Unit,
    onToggleChart: () -> Unit,
    showPieChart: Boolean
) {
    val selectedBillDateTime = remember(selectedBill) {
        if (selectedBill != null) {
            SimpleDateFormat("dd MMMM, EEEE HH:mm", Locale.getDefault())
                .format(
                    Date.from(selectedBill.billDate.toInstant())
                )
                //todo oczywiscie replace first char nie dziala
//                .uppercase()
                .replaceFirstChar { it.titlecase() }
        } else {
            "Shopping date"
        }
    }

    MediumTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Navigate back icon"
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedBill?.shop ?: "no shop name",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                // DateTime
                Text(
                    text = selectedBillDateTime,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.End,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = selectedBill?.address ?: "no address",
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    textAlign = TextAlign.End,
                )
            }
        }, actions = {
            IconButton(onClick = { onToggleChart() }
            ) {
                if (showPieChart) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Toggle bar chart button"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = "Toggle pie chart button"
                    )
                }
            }
            if (selectedBill != null) {
                EditOrDeleteBillAction(
                    selectedBill = selectedBill,
                    onDeleteConfirmed = onDeleteConfirmed,
                    onEditPressed = onEditPressed
                )
            }
        }
    )
}

@Composable
fun EditOrDeleteBillAction(
    selectedBill: Bill?,
    onDeleteConfirmed: () -> Unit,
    onEditPressed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            },
            onClick = {
                openDialog = true
                expanded = false
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = "Edit")
            },
            onClick = {
                onEditPressed()
                expanded = false
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )

    }
    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this bill \n '${selectedBill?.price}' \n${selectedBill?.shop}?",
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}