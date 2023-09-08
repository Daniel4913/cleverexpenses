package com.example.cleverex.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.datastore.BudgetDataStore
import com.example.cleverex.domain.billOverview.FetchAllBillsUseCase
import com.example.cleverex.domain.BillsByWeeks
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber


class HomeViewModel(
    private val fetchAllBillsUseCase: FetchAllBillsUseCase,
    private val budgetDataStore: BudgetDataStore
) : ViewModel() {

    var weekBudget by mutableStateOf(WeekBudget())
        private set

    var bills: MutableState<RequestState<BillsByWeeks>> = mutableStateOf(RequestState.Idle)
    private val budgetFlow: Flow<Double> = budgetDataStore.budgetFlow


    init {
        fetchAllBills()
        setUi()
    }

    private fun setUi() {
        viewModelScope.launch {
            budgetFlow.collect { budget ->
                weekBudget = weekBudget.copy(budget = budget)
            }
        }
    }

    private fun fetchAllBills() {
        viewModelScope.launch {
            bills.value = RequestState.Success(data = fetchAllBillsUseCase.execute())
        }
    }
}

data class WeekBudget(
    val budget: Double = 0.0
)