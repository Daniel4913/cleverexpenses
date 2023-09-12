package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZonedDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewScreen(
    uiState: UiState,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onToggleChart: () -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }

    Scaffold(
        topBar = {
            BillOverviewTopBar(
                selectedBill = uiState.selectedBill,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
                onEditPressed = onEditPressed,
                onToggleChart = onToggleChart,
                showPieChart = uiState.showPieChart
            )
        },
        content = { paddingValues ->
            padding = paddingValues
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                BillOverviewContent(
                    selectedBill = uiState.selectedBill,
                    billItems = uiState.billItems,
                    paddingValues = paddingValues,
                    showPieChart = uiState.showPieChart
                )
            }
        })
}
