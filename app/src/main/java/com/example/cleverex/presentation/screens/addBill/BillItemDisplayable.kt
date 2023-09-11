package com.example.cleverex.presentation.screens.addBill

import com.example.cleverex.displayable.category.CategoryDisplayable
import io.realm.kotlin.ext.realmListOf
import org.mongodb.kbson.ObjectId

data class BillItemDisplayable(
    var id: ObjectId? = null,
    var name: String = "",
    var quantity: Double = 0.0,
    var unitPrice: Double = 0.0,
    var totalPrice: Double = 0.0,
    var unit: String = "",
    var categories: List<CategoryDisplayable> = realmListOf()
)
