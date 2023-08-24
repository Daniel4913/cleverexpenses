package com.example.cleverex.presentation.screens.addBill

import android.net.Uri
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
import com.example.cleverex.presentation.screens.addItems.ImageData
import com.example.cleverex.presentation.screens.addItems.ImageState
import com.example.cleverex.util.Constants.ADD_BILL_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.time.Instant
import java.time.ZonedDateTime

class AddBillViewModel(
    val fetchBillUseCase: FetchBillUseCase,
    val billsRepo: BillsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val billTranscription = ""

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillIdArgument()
        fetchSelectedBill()
    }

    val imageState = ImageState()

    fun addImage(imageUri: Uri) {
        imageState.addImage(ImageData(imageUri = imageUri, extractedText = null))
    }

    private fun getBillIdArgument() {
        // copy function to change only one property and not everything
        uiState = uiState.copy(
            selectedBillId = savedStateHandle.get<String>(
                key = ADD_BILL_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedBill() {
        if (uiState.selectedBillId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                billsRepo.getSelectedBill(
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
                            bill.data.billImage?.let { setBillImage(billImage = it) }
                        }
                    }
            }
        }
    }

    private fun setSelectedBill(bill: Bill) {
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

    private fun setBillImage(billImage: String) {
        uiState = uiState.copy(billImage = billImage)
    }


    private suspend fun insertBill(
        bill: Bill,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Timber.tag("insert").d("${uiState}")
        val result = billsRepo.insertNewBill(bill.apply {
            if (uiState.updatedDateAndTime != null) {
                Timber.tag("insert").d("${uiState.updatedDateAndTime}")
                billDate = uiState.updatedDateAndTime!!
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }
    }

    fun upsertBill(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("upsert launched")
            if (uiState.selectedBillId != null) {
                Timber.d("upsert uiState.selectedBillId != null ${uiState.selectedBillId}")
                uiState.selectedBill?.let {
                    updateBill(
                        bill = it,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            } else {
                Timber.d("upsertBill invoked else")
                insertBill(
                    bill = Bill().apply {
                        shop = uiState.shop
                        address = uiState.address
                        billDate = uiState.updatedDateAndTime ?: Instant.now().toRealmInstant()
                        price = uiState.price
                        billItems = uiState.billItems
                        billImage = uiState.billImage
                        paymentMethod = uiState.paymentMethod
                        billTranscription = billTranscription
                    }, onSuccess = onSuccess,
                    onError = onError
                )
            }
        }
    }

    private suspend fun updateBill(
        bill: Bill,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val result = billsRepo.updateBill(bill.apply {
            _id = ObjectId.invoke(uiState.selectedBillId!!)
            billDate = if (uiState.updatedDateAndTime != null) {
                uiState.updatedDateAndTime!!
            } else {
                uiState.selectedBill?.billDate!!
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }

    }

    fun deleteBill(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedBillId != null) {
                val result =
                    billsRepo.deleteBill(id = ObjectId.invoke(uiState.selectedBillId!!))
                if (result is RequestState.Success) {
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else if (result is RequestState.Error) {
                    withContext(Dispatchers.Main) {
                        onError(result.error.message.toString())
                    }
                }
            }
        }
    }
}

// data class to hold all informations and values in add bill screen(write in dairy)
data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: RealmList<BillItem> = realmListOf(),
    val billImage: String = "",
    val paymentMethod: String = ""
)