package com.example.cleverex.presentation.screens.billOverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.FetchBillUseCase
import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.time.ZonedDateTime

class BillOverviewViewModel(
    val fetchBillUseCase: FetchBillUseCase,
    val billsRepo: BillsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillIdArgument()
        fetchSelectedBill()
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
            Timber.d("selectedBillId != null: ${uiState.selectedBillId}; selectedBill ${uiState.selectedBill}")
            viewModelScope.launch(Dispatchers.Main) {
                billsRepo.getSelectedBill(
                    billId = ObjectId.invoke(uiState.selectedBillId!!)
                )
                    .catch {
                        emit(RequestState.Error(Exception("Bill is already deleted")))
                    }
                    .collect { bill ->
                        if (bill is RequestState.Success) {
                            Timber.d("selectedBill: ${bill.data}")
                            setSelectedBill(bill = bill.data)
                            setShop(shop = bill.data.shop)
                            setAddress(address = bill.data.address)
                            setPrice(price = bill.data.price)
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

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(updatedDateAndTime = zonedDateTime.toInstant().toRealmInstant())
    }

    fun setPrice(price: Double) {
        uiState = uiState.copy(price = price)
    }

    fun setBillImage(billImage: String) {
        uiState = uiState.copy(billImage = billImage)
    }
}

data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: RealmList<BillItem> = realmListOf(),
    val billImage: String = ""
)