package com.example.cleverex.presentation.screens

import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.OcrLogs
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList

data class UiState(
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val chosenImage: ImageData? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: RealmList<BillItem> = realmListOf(),
    val billImage: String = "",
    val paymentMethod: String = "",
    val billTranscription: RealmList<OcrLogs> = realmListOf() // ale tego nie potrzebuje w UI state, inaczej moszę to przekazywać do upsert
)