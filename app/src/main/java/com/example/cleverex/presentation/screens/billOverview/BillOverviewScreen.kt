package com.example.cleverex.presentation.screens.billOverview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BillOverviewScreen(
    billId: String
) {
    var padding by remember { mutableStateOf(PaddingValues())}

    Scaffold(
        content = {
            padding = it
        
        Text(text = "billId: $billId")
    })
}
