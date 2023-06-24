package com.example.cleverex.displayable

import com.example.cleverex.model.BillItem
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList

data class BillDisplayable (
    val selectedBillId: String? = null,
    val shop: String = "",
    val address: String = "",
    val billDate: RealmInstant? = null,
    val price: Double = 0.0,
    val billItems: RealmList<BillItem> = realmListOf(),
    val billImage: String = ""
)