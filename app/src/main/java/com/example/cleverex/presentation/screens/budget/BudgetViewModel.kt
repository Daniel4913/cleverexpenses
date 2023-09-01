package com.example.cleverex.presentation.screens.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import timber.log.Timber

class BudgetViewModel : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    fun setBudget(budget: Double) {
        uiState = uiState.copy(budget = budget)
        Timber.d("${uiState.budget}")
    }

}

data class UiState(
    val budget: Double = 100.00
)