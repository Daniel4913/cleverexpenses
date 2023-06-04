package com.example.cleverex.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.Bills
import com.example.cleverex.data.BillsByWeeks
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.data.FakeBillsDb
import com.example.cleverex.data.MongoDB
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