package com.example.cleverex.presentation.screens.addItems

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.OcrLogs
import com.example.cleverex.domain.addItems.InsertItemsUseCase
import com.google.mlkit.vision.text.Text
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList

class AddItemsViewModel(
    val InsertItemsUseCase: InsertItemsUseCase
) : ViewModel() {
    val imageState = ImageState()



    fun addImage(imageUri: Uri){
        imageState.addImage(ImageData(imageUri =imageUri, extractedText = null))
    }
}

class ImageState {
    val image = mutableStateListOf<ImageData>()

    fun addImage(imageData: ImageData){
        image.clear()
        image.add(imageData)
    }
}

data class ImageData(
    val imageUri: Uri,
    val extractedText: Text? = null
)

data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val billItems: List<BillItem> = mutableListOf(),
    val billImage: String = "",
)