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
import com.example.cleverex.domain.billOverview.FetchBillUseCase
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.addBill.ListBillItemDisplayableListToBillItemMapper
import com.example.cleverex.domain.addBill.ListBillItemToListToBillItemDisplayableMapper
import com.example.cleverex.domain.addBill.UploadToFirebaseUseCase
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.util.Constants.ADD_BILL_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toRealmInstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
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
    val toBillItems: ListBillItemDisplayableListToBillItemMapper,
    val toBillItemsDisplayable: ListBillItemToListToBillItemDisplayableMapper,
    val uploadToFirebase: UploadToFirebaseUseCase,
    val billsRepo: BillsRepository,
    val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getBillIdArgument()
        fetchSelectedBill()
        populateCategories()
    }

    val imageState = ImageState()


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
                            populateBillItems(bill.data.billItems)
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

    private fun populateCategories() {
        viewModelScope.launch {
            uiState = uiState.copy(
                allCategories = fetchCategoriesUseCase.fetch()
            )
        }
    }

    private fun populateBillItems(billItems: RealmList<BillItem>) {
        uiState = uiState.copy(
            billItemsDisplayable = toBillItemsDisplayable.map(billItems).toMutableList()
        )
    }

    fun toggleSelectedCategory(categoryId: ObjectId?, picked: Boolean) {
        val allCategoriesMutable = uiState.allCategories.toMutableList()
        val selectedCategoriesMutable = uiState.selectedCategories.toMutableList()

        val foundCategoryIndex =
            allCategoriesMutable.indexOfFirst { it.id == categoryId }

        if (foundCategoryIndex != -1) {
            val foundCategory =
                allCategoriesMutable[foundCategoryIndex]
            foundCategory.categoryPicked = picked

            if (picked) {
                selectedCategoriesMutable.add(foundCategory)
            } else {
                selectedCategoriesMutable.remove(foundCategory)
            }

            uiState = uiState.copy(
                allCategories = allCategoriesMutable,
                selectedCategories = selectedCategoriesMutable
            )
        } else {
            Timber.d(">>>>>>No category with this ObjectId: $categoryId")
        }
    }

    var productToUpdateIndex = -1
    fun createAndAddBillItemDisplayable() {
        val newBillItem = BillItemDisplayable(
        ).apply {
            name = uiState.name
            quantity = parseBillItem(uiState.unparsedValues).first ?: 0.0
            unitPrice = parseBillItem(uiState.unparsedValues).second ?: 0.0
            totalPrice = parseBillItem(uiState.unparsedValues).third ?: 0.0
            categories = uiState.selectedCategories
        }

        val currentItems = uiState.billItemsDisplayable

        if (productToUpdateIndex > -1) {
            currentItems[productToUpdateIndex] = newBillItem
            productToUpdateIndex = -1
        } else {
            currentItems.add(newBillItem)
        }

        val updatedUiState =
            uiState.copy(billItemsDisplayable = currentItems)

        uiState = updatedUiState
        clearProductFields()
    }

    private fun clearProductFields() {
        uiState.selectedCategories.forEach {
            toggleSelectedCategory(it.id, false)
        }
        uiState = uiState.copy(
            name = "",
            unparsedValues = "",
        )
    }

    fun editBillItem(productIndex: Int) {
        val selectedProduct = uiState.billItemsDisplayable[productIndex]
        selectedProduct.categories.forEach {
            toggleSelectedCategory(it.id, true)
        }

        uiState = uiState.copy(
            name = selectedProduct.name,
            unparsedValues = "${selectedProduct.quantity} ${selectedProduct.unitPrice} ${selectedProduct.totalPrice}",
        )

        productToUpdateIndex = productIndex
    }

    private fun chosenImage(chosenImage: ImageData?) {
        uiState = uiState.copy(chosenImage = chosenImage)
    }

    private fun populateBillFromUIState(existingBill: Bill? = null): Bill {
        return (existingBill ?: Bill()).apply {
            shop = uiState.shop
            address = uiState.address
            billDate = uiState.updatedDateAndTime ?: Instant.now().toRealmInstant()
            price = uiState.price
            billItems = toBillItems.map(uiState.billItemsDisplayable)
            billImage = imageState.getRemoteImagePath()
            paymentMethod = uiState.paymentMethod
        }
    }


    fun upsertBill(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedBillId != null) {
                val billToUpdate = Bill().apply {
                    _id = ObjectId.invoke(uiState.selectedBillId!!)
                    shop = uiState.shop
                    address = uiState.address
                    price = uiState.price
                    billItems = toBillItems.map(uiState.billItemsDisplayable)
                    billImage = imageState.getRemoteImagePath()
                    paymentMethod = uiState.paymentMethod
                }
                updateBill(
                    bill = billToUpdate,
                    onSuccess = onSuccess,
                    onError = onError
                )
            } else {
                val newBill = populateBillFromUIState()
                insertBill(bill = newBill, onSuccess = onSuccess, onError = onError)
            }
        }
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
            uploadImagesToFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }
    }

    private suspend fun updateBill(
        bill: Bill,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = billsRepo.updateBill(
            bill
        )
        if (result is RequestState.Success) {
            uploadImagesToFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
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
        val downloadedBillImage: Uri? = null,
        val paymentMethod: String = "",
        val name: String = "",
        val quantity: String = "",
        val productPrice: String = "",
        val quantityTimesPrice: String = "",
        val unparsedValues: String = "",
        val allCategories: List<CategoryDisplayable> = listOf(),
        val selectedCategories: MutableList<CategoryDisplayable> = mutableListOf(),
    )

    fun addImage(imageUri: Uri, imageType: String) {
        val remoteImagePath = "bills/${FirebaseAuth.getInstance().currentUser?.uid}/" +
                "${imageUri.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        imageState.addImage(
            ImageData(
                imageUri = imageUri,
                remoteImagePath = remoteImagePath,
                extractedText = null
            )
        )
        chosenImage(imageState.image.firstOrNull())
    }

    private fun uploadImagesToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        imageState.image.forEach { imageData ->
            val imagePath = storage.child(imageData.remoteImagePath)
            imagePath.putFile(imageData.imageUri)
        }
    }

    class ImageData(
        val imageUri: Uri,
        val remoteImagePath: String,
        val extractedText: String?
    )

    class ImageState {
        val image = mutableStateListOf<ImageData>()

        fun addImage(imageData: ImageData) {
            image.clear()
            image.add(imageData)
        }

        fun getRemoteImagePath(): String? {
            return image.firstOrNull()?.remoteImagePath
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
