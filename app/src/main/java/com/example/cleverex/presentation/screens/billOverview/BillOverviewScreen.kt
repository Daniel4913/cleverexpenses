package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleverex.presentation.screens.addBill.BillTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewScreen(
    uiState: UiState,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }

    Scaffold(
        topBar = {
            BillTopBar(
                selectedBill = uiState.selectedBill,
                onDateTimeUpdated = {},
                onDeleteConfirmed = { /*TODO*/ },
                onBackPressed = onBackPressed
            )


        },
        content = {
            padding = it
            Column(modifier = Modifier.padding(60.dp)) {
                Text(
                    text = "billId: ${uiState.selectedBillId}"
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "bill: ${uiState.selectedBill}"
                )
            }
            BillOverviewContent()

        })
}
