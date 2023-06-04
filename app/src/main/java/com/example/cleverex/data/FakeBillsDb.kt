package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.*
import org.mongodb.kbson.ObjectId
import java.util.*


class FakeBillsDb : BillsRepository {
    override fun getAllBills(): Flow<BillsByWeeks> {
        val data = RequestState.Success(
            data = fakeBills.groupBy {
                val billInstant = it.billDate.toInstant()
                val calendar = Calendar.getInstance()
                calendar.time = Date.from(billInstant)
                calendar.get(Calendar.WEEK_OF_YEAR)
            })
        return flow { emit(data) }
    }

    override fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>> {
        fakeBills.forEach {
            return if (it._id == billId) {
                flow { RequestState.Success(data = it) }
            } else {
                flow { RequestState.Error(Exception("No bill with this id found")) }
            }
        }
        return flow { RequestState.Error(Exception("No bill with this id found")) }

    }

    override suspend fun insertNewBill(bill: Bill): RequestState<Bill> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBill(bill: Bill): RequestState<Bill> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBill(id: ObjectId): RequestState<Bill> {
        TODO("Not yet implemented")
    }


    private val fakeBills =
        listOf(
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa71")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 1"
                billDate = RealmInstant.from(1682967432, 0)
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
                _id = ObjectId("644a6ccfc0512c56e895fa72")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 2"
                billDate = RealmInstant.from(1683053832, 0)
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
                _id = ObjectId("644a6ccfc0512c56e895fa73")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 3"
                billDate = RealmInstant.from(1684004232, 0)
                price = 33.33
                billItems = realmListOf(
                    BillItem().apply { },
                    BillItem().apply { },
                    BillItem().apply { }
                )
                billImage = ""
                billTranscription = ""
            },
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa74")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 4"
                billDate = RealmInstant.from(1684090632, 0)
                price = 44.44
                billItems = realmListOf(
                    BillItem().apply { },
                    BillItem().apply { },
                    BillItem().apply { }
                )
                billImage = ""
                billTranscription = ""
            },
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa75")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 5"
                billDate = RealmInstant.from(1684177032, 0)
                price = 55.55
                billItems = realmListOf(
                    BillItem().apply { },
                    BillItem().apply { },
                    BillItem().apply { }
                )
                billImage = ""
                billTranscription = ""
            },
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa66")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 6"
                billDate = RealmInstant.from(1685559432, 0)
                price = 66.66
                billItems = realmListOf(
                    BillItem().apply { },
                    BillItem().apply { },
                    BillItem().apply { }
                )
                billImage = ""
                billTranscription = ""
            },
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa77")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 7"
                billDate = RealmInstant.from(1685645832, 0)
                price = 77.77
                billItems = realmListOf(
                    BillItem().apply { },
                    BillItem().apply { },
                    BillItem().apply { }
                )
                billImage = ""
                billTranscription = ""
            },
            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa78")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 8"
                billDate = RealmInstant.from(1685732232, 0)
                price = 88.88
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


