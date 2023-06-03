package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY


class BillOverviewViewModel(
    //tutaj z diary
    private val savedStateHandle: SavedStateHandle,
    // todo tutaj z newsapp
//    billId: String,
//    fetchBill: FetchBillUseCase

) : ViewModel() {

    var uiState by mutableStateOf(UiState())

    init {
        getBillIdArgument()
    }

   private fun getBillIdArgument() {
        uiState = uiState.copy(
            selectedBillIdd = savedStateHandle.get<String>(key = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY)
        )
    }
}

data class UiState(
    val selectedBillIdd: String? = "", // savedStateHandle zwraca nullable string, . Dlaczego :(
    val shopName: String = "",
    val billPrice: Double = 0.0
//...
)