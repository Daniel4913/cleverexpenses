package com.example.cleverex.presentation.screens.billOverview

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.domain.billOverview.FetchBillUseCase
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.billOverview.DeleteBillUseCase
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.fetchImagesFromFirebase
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import timber.log.Timber

class BillOverviewViewModel(
    private val fetchBillUseCase: FetchBillUseCase,
    private val deleteBillUseCase: DeleteBillUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillIdArgument()
        fetchSelectedBill()
    }

    fun downloadImages() {

        fetchImagesFromFirebase(
            remoteImagePaths = listOf(uiState.billImage),
            onImageDownload = {
                uiState = uiState.copy(
                    downloadedBillImage = it
                )
                Timber.d("Downloaded image $it")
            },
            onImageDownloadFailed = {
                Timber.d("Failed to download image $it ${it.message}")
            },
            onReadyToDisplay = {})
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

    fun deleteBill(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteBillUseCase.execute(
                uiState.selectedBillId!!,
                onSuccess = onSuccess,
                onError = onError
            )
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

    fun toggleShowBillImage() {
        uiState = if (uiState.showBillImage) {
            uiState.copy(
                showBillImage = false
            )
        } else {
            uiState.copy(
                showBillImage = true
            )
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
    val billImage: String = "",
    val downloadedBillImage: Uri? = null,
    val showBillImage: Boolean = false,
    val showPieChart: Boolean = false
)