package com.example.cleverex.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.domain.FetchAllBillsUseCase
import com.example.cleverex.displayable.BillsByWeeks
import com.example.cleverex.util.RequestState

import kotlinx.coroutines.launch
import timber.log.Timber


class HomeViewModel(
    private val fetchAllBillsUseCase: FetchAllBillsUseCase,
    ) : ViewModel() {

    var bills: MutableState<RequestState<BillsByWeeks>> = mutableStateOf(RequestState.Idle)

    init {
        fetchAllBills()
    }

    private fun fetchAllBills() {
        Timber.d("fetching all bills")
        viewModelScope.launch {
            bills.value = RequestState.Success(data = fetchAllBillsUseCase.execute())
        }
    }
}