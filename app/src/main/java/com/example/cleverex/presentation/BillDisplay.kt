package com.example.cleverex.presentation

import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList

data class BillDisplay (
    val selectedBillId: String? = null,
    val selectedBill: Bill? = null,
    val shop: String = "",
    val address: String = "",
    val updatedDateAndTime: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: RealmList<BillItem> = realmListOf(),
    val billImage: String = ""
)
