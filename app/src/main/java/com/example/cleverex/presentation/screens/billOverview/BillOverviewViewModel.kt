package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.displayable.bill.BillToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.CreateCategoryUseCase
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toRealmInstant
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.time.ZonedDateTime

class BillOverviewViewModel(
    private val fetchBillUseCase: FetchBillUseCase,
    private val fetchItemUseCase: FetchItemUseCase,
    private val displayableMapper: BillToDisplayableMainMapper,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillIdArgument()
        fetchSelectedBill()
    }

    fun togglePieChart() {
        uiState = if (uiState.showPieChart) {
            uiState.copy(
                showPieChart = false
            )
        } else {
            uiState.copy(
                showPieChart = true
            )
        }
    }

    private fun getBillIdArgument() {
        uiState = uiState.copy(
            selectedBillId = savedStateHandle.get<String>(
                key = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedBill() {
        if (!uiState.selectedBillId.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.Main) {
                fetchBillUseCase.fetchBill(
                    billId = ObjectId.invoke(uiState.selectedBillId!!)
                )
                    .catch {
                        emit(RequestState.Error(Exception("Bill is already deleted")))
                    }
                    .collect { bill ->
                        if (bill is RequestState.Success) {
                            setSelectedBill(bill = bill.data)
                            setShop(shop = bill.data.shop)
                            setAddress(address = bill.data.address)
                            setPrice(price = bill.data.price)
                            setBillItems(billItems = bill.data.billItems.toList())
                            bill.data.billImage?.let { setBillImage(billImage = it) }
                        }
                    }
            }
        }
    }

    fun setSelectedBill(bill: Bill) {
        uiState = uiState.copy(selectedBill = bill)
    }

    fun setShop(shop: String) {
        uiState = uiState.copy(shop = shop)
    }

    fun setAddress(address: String?) {
        uiState = uiState.copy(address = address ?: "No address added")
    }

    fun setPrice(price: Double) {
        uiState = uiState.copy(price = price)
    }

    fun setBillImage(billImage: String) {
        uiState = uiState.copy(billImage = billImage)
    }

    fun setBillItems(billItems: List<BillItem>) {
        uiState = uiState.copy(billItems = billItems)
    }
}

class FetchItemUseCase(
    private val repository: BillsRepository
) {
//    suspend fun fetchItem() {
//        repository.getItem()
//    }

}

data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: List<BillItem> = listOf(),
    val billImage: String = "",
    val showPieChart: Boolean = false
)