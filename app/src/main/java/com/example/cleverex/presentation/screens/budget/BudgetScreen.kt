package com.example.cleverex.presentation.screens.budget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onBackPressed: () -> Unit,
    weeklyBudget: (String),
    onBudgetChange: (String) -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Navigate back icon"
                        )
                    }
                },
                title = { Text(text = "Set weekly budget") },
                actions = {
                    IconButton(onClick = {
                    }) {
                        Icons.Rounded.Build
                    }
                }
            )
        }) {
        padding = it
        BudgetContent(
            budget = weeklyBudget,
            onBudgetChange = onBudgetChange
        )  // Pass the hoisted state down
    }
}

@Composable
fun BudgetContent(
    budget: String,  // Hoisted state received as a parameter
    onBudgetChange: (String) -> Unit  // Callback to update the hoisted state
) {
    TextField(
        value = budget,
        onValueChange = onBudgetChange,  // Update the hoisted state
        placeholder = { Text("Enter your weekly budget") }
    )
}