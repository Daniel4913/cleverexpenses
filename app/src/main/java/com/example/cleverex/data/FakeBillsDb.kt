package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toRealmInstant
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import org.mongodb.kbson.ObjectId
import java.time.Instant

object FakeBillsDb : FakeBillRepository {

    override fun getAllFakeBills(): Flow<List<Bill>> {
        return flow { fakeBills }
    }

    override fun getSelectedFakeBill(billId: ObjectId): Flow<RequestState<Bill>> {
        var queriedBill = RequestState.Success(Bill())

        fakeBills.forEach {
            if (it._id == billId) {
                queriedBill = RequestState.Success(it)
            } else {
                RequestState.Error(Exception("No bill with this id"))
            }
        }
        return flow { queriedBill }
    }

    private val fakeBills = listOf(

        Bill().apply {
            ownerId = "6429ec6ab5591ec35eb2a0ef"
            shop = "Lidl"
            address = "Lidlowa 1"
            billDate = Instant.now().toRealmInstant()
            price = 11.11
            billItems = realmListOf(
                BillItem().apply { },
                BillItem().apply { },
                BillItem().apply { }
            )
            billImage = ""
            billTranscription = ""
        },
        Bill().apply {
            ownerId = "6429ec6ab5591ec35eb2a0ef"
            shop = "Lidl"
            address = "Lidlowa 2"
            billDate = Instant.now().toRealmInstant()
            price = 22.22
            billItems = realmListOf(
                BillItem().apply { },
                BillItem().apply { },
                BillItem().apply { }
            )
            billImage = ""
            billTranscription = ""
        },
        Bill().apply {
            ownerId = "6429ec6ab5591ec35eb2a0ef"
            shop = "Lidl"
            address = "Lidlowa 3"
            billDate = Instant.now().toRealmInstant()
            price = 33.33
            billItems = realmListOf(
                BillItem().apply { },
                BillItem().apply { },
                BillItem().apply { }
            )
            billImage = ""
            billTranscription = ""
        }
    )


}


