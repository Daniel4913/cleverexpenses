package com.example.cleverex.domain.addBill

import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.ListCategoryDisplayableToListEmbeddedMapper
import com.example.cleverex.domain.browseCategory.MainMapper
import com.example.cleverex.presentation.screens.addBill.BillItemDisplayable
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

class ListBillItemDisplayableListToBillItemMapper(
    private val toEmbeddedMapper: ListCategoryDisplayableToListEmbeddedMapper
) : MainMapper<List<BillItemDisplayable>, RealmList<BillItem>> {
    override fun map(from: List<BillItemDisplayable>): RealmList<BillItem> {
        return realmListOf(*from.map { billItemDisplayable ->
            BillItem().apply {
                name = billItemDisplayable.name
                quantity = billItemDisplayable.quantity
                price = billItemDisplayable.unitPrice
                totalPrice = billItemDisplayable.totalPrice
                categories = toEmbeddedMapper.map(billItemDisplayable.categories)
            }
        }.toTypedArray())
    }
}