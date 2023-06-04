package com.example.cleverex.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.presentation.displayable.BillsByWeeks
import com.example.cleverex.util.RequestState

import kotlinx.coroutines.launch



class HomeViewModel (
    private val billsRepo: BillsRepository
) : ViewModel() {

    var bills: MutableState<BillsByWeeks> = mutableStateOf(RequestState.Idle)

    init {
        observeAllBills()
    }

    private fun observeAllBills() {
        viewModelScope.launch {
            billsRepo.getAllBills().collect { result ->
                bills.value = result
            }
        }
    }

}