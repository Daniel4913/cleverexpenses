package com.example.cleverex.presentation.screens.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.datastore.BudgetDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val budgetDataStore: BudgetDataStore
) : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    val budgetFlow: Flow<Double> = budgetDataStore.budgetFlow

    init {
        setUi()
    }

    private fun setUi() {
        viewModelScope.launch {
            budgetFlow.collect { budget ->
                setUiState(budget)
            }
        }
    }

    fun setBudget(budget: Double) {
        viewModelScope.launch {
            budgetDataStore.saveBudget(budget)
        }

    }

    private fun setUiState(budget: Double) {
        uiState = uiState.copy(budget = budget)
    }

}

data class UiState(
    val budget: Double = 100.00
)