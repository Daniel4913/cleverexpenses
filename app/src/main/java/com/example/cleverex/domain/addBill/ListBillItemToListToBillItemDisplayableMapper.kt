package com.example.cleverex.domain.addBill

import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.ListCategoryEmbeddedToListDisplayableMapper
import com.example.cleverex.domain.browseCategory.MainMapper
import com.example.cleverex.presentation.screens.addBill.BillItemDisplayable
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.BsonObjectId

class ListBillItemToListToBillItemDisplayableMapper(
    private val toListCategoryDisplayable: ListCategoryEmbeddedToListDisplayableMapper
) :
    MainMapper<RealmList<BillItem>, List<BillItemDisplayable>> {
    override fun map(from: RealmList<BillItem>): List<BillItemDisplayable> {
        return realmListOf(*from.map { billItem ->
            BillItemDisplayable().apply {
                id = org.mongodb.kbson.ObjectId()
                name = billItem.name
                quantity = billItem.quantity
                unitPrice = billItem.price
                totalPrice = billItem.totalPrice
                unit = billItem.unit
                categories = toListCategoryDisplayable.map(billItem.categories)
            }
        }.toTypedArray())
    }

}
