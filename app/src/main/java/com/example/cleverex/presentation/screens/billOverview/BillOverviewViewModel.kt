package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY

class BillOverviewViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillId()
    }

    private fun getBillId(){
        uiState = uiState.copy(
            billId = savedStateHandle.get<String>(key = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY)
        )
    }

}

data class UiState(
    val billId: String? = "",
    val price: Double = 0.0
)