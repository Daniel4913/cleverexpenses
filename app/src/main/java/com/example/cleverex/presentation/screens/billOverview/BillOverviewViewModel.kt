package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.displayable.bill.BillToDisplayableMapper
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class BillOverviewViewModel(
    private val fetchBillUseCase: FetchBillUseCase,
    private val displayableMapper: BillToDisplayableMapper,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // w viewModelu zmapuje Bill z fetchBillUseCase na BillDisplayable

    var uiState by mutableStateOf(UiState())
        private set

    init {
        val billId = savedStateHandle.get<String>(
            key = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
        )
        billId?.let { fetchSelectedBill(billId = it) }
    }


    private fun fetchSelectedBill(billId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            fetchBillUseCase.fetchBill(
                billId = ObjectId.invoke(billId)
            ).collect { requestState ->
                when (requestState) {
                    is RequestState.Success -> {
                        uiState = uiState.copy(
                            selectedBillId = requestState.data._id.toHexString(),
                            selectedBill = requestState.data,
                            shop = requestState.data.shop,
                            address = requestState.data.address ?: "No address added",
                            updatedDateAndTime = requestState.data.billDate,
                            price = requestState.data.price,
                            billItems = requestState.data.billItems,
                            billImage = requestState.data.billImage ?: "",
                            errorMessage = null,
                            loading = false
                        )
                    }

                    is RequestState.Error -> {
                        uiState = uiState.copy(
                            errorMessage = requestState.error.message,
                            loading = false

                        )
                    }

                    RequestState.Loading -> {
                        uiState = uiState.copy(
                            loading = true
                        )
                    }
                }
            }
        }
    }

}

data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: List<BillItem> = listOf(),
    val billImage: String? = "",
    val errorMessage: String? = null,
    val loading: Boolean = false
)