package com.example.cleverex.presentation.screens.addBill

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
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
    val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    var uiState by mutableStateOf(UiState())
        private set

//    In Jetpack Compose, you can use StateFlow instead of LiveDataStateFlow is a type of Flow that represents a read-only state with a single updatable data value that emits updates to the value to its collectors1. However, there are some lifecycle implications of observing/collecting StateFlow in UI code when compared to the use of LiveData2. For example, LiveData.observe() automatically unregisters the consumer when the view goes to the STOPPED state, whereas collecting from a StateFlow or any other flow does not2. To avoid wasting resources, you need to manually stop collecting the flow when the UI is not on the screen2.
//    private val _billItems = MutableLiveData<List<BillItem>>(emptyList())
//    val billItems: LiveData<List<BillItem>> = _billItems

    init {
        getBillIdArgument()
        fetchSelectedBill()
        populateCategories()
    }

    val imageState = ImageState()

    fun addImage(imageUri: Uri) {
        imageState.addImage(ImageData(imageUri = imageUri, extractedText = null))
        chosenImage(imageState.image.firstOrNull())
    }


    private fun getBillIdArgument() {
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

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun setQuantity(quantity: String) {
        uiState = uiState.copy(quantity = quantity)
    }

    fun setProductPrice(productPrice: String) {
        uiState = uiState.copy(productPrice = productPrice)
    }

    fun setQuantityTimesPrice(quantityTimesPrice: String) {
        uiState = uiState.copy(quantityTimesPrice = quantityTimesPrice)
    }

    fun setUnparsedValues(unparsedValues: String) {
        uiState = uiState.copy(unparsedValues = unparsedValues)
    }

    fun populateCategories() {
        viewModelScope.launch {
            uiState = uiState.copy(
                allCategories = fetchCategoriesUseCase.fetch()
            )
        }
    }

    // TODO wyczyścić listę po dodaniu billItem no i wyczyścić wszystko w pizdu po dodaniu całego Bill
    fun toggleSelectedCategory(categoryId: ObjectId, picked: Boolean) {
        val allCategoriesMutable = uiState.allCategories.toMutableList()
        val selectedCategoriesMutable = uiState.selectedCategories.toMutableList()

        val foundCategoryIndex = allCategoriesMutable.indexOfFirst { it.id == categoryId }

        if (foundCategoryIndex != -1) {
            // Znaleziono kategorię, aktualizacja pola categoryPicked
            val foundCategory = allCategoriesMutable[foundCategoryIndex]
            foundCategory.categoryPicked = picked

            if (picked) {
                // Dodanie kategorii do listy selectedCategories
                selectedCategoriesMutable.add(foundCategory)
                Timber.d(">>>Category successfully added: $selectedCategoriesMutable")
            } else {
                // Usunięcie kategorii z listy selectedCategories
                selectedCategoriesMutable.remove(foundCategory)
                Timber.d(">>>Category successfully removed: $selectedCategoriesMutable")
            }

            // Aktualizacja stanu
            uiState = uiState.copy(
                allCategories = allCategoriesMutable,
                selectedCategories = selectedCategoriesMutable
            )
        } else {
            Timber.d(">>>>>>No category with this ObjectId: $categoryId")
        }
    }

    fun createAndAddBillItemDisplayable() {
        val newBillItem = BillItemDisplayable(
        ).apply {
            name = uiState.name
            quantity = parseBillItem(uiState.unparsedValues).first ?: 0.0
            unitPrice = parseBillItem(uiState.unparsedValues).second ?: 0.0
            totalPrice = parseBillItem(uiState.unparsedValues).third ?: 0.0
            categories = uiState.selectedCategories
        }

        // Get the current list of BillItems
        val currentItems = uiState.billItemsDisplayable

        // Add the new BillItem to the list
        currentItems.add(newBillItem)

        // Update the list in uiState
        val updatedUiState =
            uiState.copy(billItemsDisplayable = currentItems) //realmListOf(*updatedItems.toTypedArray()))

        // Update the UiState
//        uiState = updatedUiState
        uiState = updatedUiState
    }


//    fun createAndAddBillItem() {
//        val newBillItem = BillItem(
//        ).apply {
//            name = uiState.name
//            quantity = parseBillItem(uiState.unparsedValues).first ?: 0.0
//            price = parseBillItem(uiState.unparsedValues).second ?: 0.0
//            totalPrice = parseBillItem(uiState.unparsedValues).third ?: 0.0
////            categories = uiState.selectedCategories // TODO Required: RealmList<CategoryEmbedded>
//            categories = realmListOf()
//        }
//
//        // Get the current list of BillItems
//        val currentItems = uiState.billItems.toList()
//        currentItems.forEach {
//            Timber.d("${it.name}")
//        }
//
//        // Add the new BillItem to the list
//        val updatedItems = currentItems + newBillItem
//
//        // Update the list in uiState
//        val updatedUiState = uiState.copy(billItems = realmListOf(*updatedItems.toTypedArray()))
//
//        // Update the UiState
//        uiState = updatedUiState
//    }

    fun chosenImage(chosenImage: ImageData?) {
        uiState = uiState.copy(chosenImage = chosenImage)
    }


    private suspend fun insertBill(
        bill: Bill,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = billsRepo.insertNewBill(bill.apply {
            if (uiState.updatedDateAndTime != null) {
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
            if (uiState.selectedBillId != null) {
                uiState.selectedBill?.let {
                    updateBill(
                        bill = it,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            } else {

                insertBill(
                    bill = Bill().apply {
                        shop = uiState.shop
                        address = uiState.address
                        billDate = uiState.updatedDateAndTime ?: Instant.now().toRealmInstant()
                        price = uiState.price
                        billItems = uiState.billItems
                        billImage = uiState.billImage
                        paymentMethod = uiState.paymentMethod
                    },
                    onSuccess = onSuccess,
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

    data class UiState(
        val selectedBillId: String? = null,
        val selectedBill: Bill? = null,
        val chosenImage: ImageData? = null,
        val shop: String = "",
        val address: String = "",
        val updatedDateAndTime: RealmInstant? = null,
        val price: Double = 0.0,
        val billItems: RealmList<BillItem> = realmListOf(),
        val billItemsDisplayable: MutableList<BillItemDisplayable> = mutableListOf(),
        val billImage: String = "",
        val paymentMethod: String = "",
        val name: String = "",
        val quantity: String = "",
        val productPrice: String = "",
        val quantityTimesPrice: String = "",
        val unparsedValues: String = "",
        val allCategories: List<CategoryDisplayable> = listOf(),
        val selectedCategories: MutableList<CategoryDisplayable> = mutableListOf(),
    )

    class ImageData(
        val imageUri: Uri,
        val extractedText: String?
    )

    class ImageState {
        val image = mutableStateListOf<ImageData>()

        fun addImage(imageData: ImageData) {
            image.clear()
            image.add(imageData)
        }
    }
}

fun parseBillItem(input: String): Triple<Double?, Double?, Double?> {
    // Usuń ostatni znak
    val sanitizedInput = input.dropLast(1)

    // Usuń spacje wokół przecinków
    val cleanedInput = sanitizedInput.replace(", ", ",").replace(" ,", ",")

    // Użyj wyrażenia regularnego do podzielenia danych na części
    val regex = """(\d+(?:[.,]\d+)?)\s*[xX]\s*(\d+(?:[.,]\d+)?)[^\d]+(\d+(?:[.,]\d+)?)""".toRegex()
    val matchResult = regex.find(cleanedInput) ?: return Triple(null, null, null)

    // Parsuj ilość i cenę
    val quantity = matchResult.groupValues[1].replace(",", ".").toDoubleOrNull()
    val price = matchResult.groupValues[2].replace(",", ".").toDoubleOrNull()

    // Parsuj całkowitą cenę
    val totalPrice = matchResult.groupValues[3].replace(",", ".").toDoubleOrNull()

    return Triple(quantity, price, totalPrice)
}
