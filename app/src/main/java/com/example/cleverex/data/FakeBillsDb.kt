package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.presentation.displayable.BillsByWeeks
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.*
import org.mongodb.kbson.ObjectId
import java.util.*


class FakeBillsDb : BillsRepository {
    override suspend fun getAllBills(): List<Bill> {
        return fakeBills
    }

    override fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>> {
        val billl = fakeBills.find { it._id == billId }
        return if (billl != null) {
            flow { emit(RequestState.Success(data = billl)) }
        } else {
            flow { RequestState.Error(Exception("no bill find")) }
        }

        // Dlaczego for i foreach nie dzialaly i zwracaly tylko pierwszy element z listy?

//        for (bill in fakeBills) {
//            return if (bill._id == billId) {
//                flow { emit(RequestState.Success(data = bill)) }
//            } else {
//                flow { emit(RequestState.Error(Exception("No bill with this id found"))) }
//            }
//        }

//        fakeBills.forEach {
//            return if (it._id == billId) {
//                Timber.d("Succesfully fetched bill: ${it._id} == $billId")
//                flow { emit(RequestState.Success(data = it)) }
//            } else {
//                Timber.d("UnSuccesfully fetched bill: ${it._id} == $billId")
//                flow { emit(RequestState.Error(Exception("No bill with this id found"))) }
//            }
//        }
//        return flow { emit(RequestState.Error(Exception("No bill with this id found"))) }
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
                address = "Generała Karpińskiego 4, 81-173" //2023-05-27
                billDate = RealmInstant.from(1682967432, 0)
                price = 11.11
                billItems = realmListOf(
                    BillItem().apply {
                        name = "Bushmills Whisk."
                        price = 79.99
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 79.99
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Chipsy Ziel.Cebulka"
                        price = 9.49
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 9.49
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Chipsy Monster Mun"
                        price = 7.29
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 9.49
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "DeserPiankaŚm."
                        price = 1.65
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 1.65
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Jogobella Jogurt"
                        price = 1.70
                        quantity = 2.0
                        unit = "szt"
                        totalPrice = 3.40
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Austriack.wędliny"
                        price = 7.99
                        quantity = 2.0
                        unit = "szt"
                        totalPrice = 15.98
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Jogurt owocowy 2,6%"
                        price = 1.35
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 15.98
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Gouda plast. 300g"
                        price = 9.99
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 9.99
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Kajzerka wieloziar."
                        price = 0.69
                        quantity = 6.0
                        unit = "szt"
                        totalPrice = 4.14
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Croissant orzech"
                        price = 2.66
                        quantity = 4.0
                        unit = "szt"
                        totalPrice = 10.54 // funfact w niemczech 0.75 * 4 = 3.0 * 4.6 = 13.8 zl
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Burek ser szpinak"
                        price = 2.29
                        quantity = 1.0
                        unit = "szt"
                        totalPrice = 2.29
                        category = "Dom"
                    },
                    BillItem().apply {
                        name = "Snack B.Car.P.baton"
                        price = 2.99
                        quantity = 4.0
                        unit = "szt"
                        totalPrice = 11.69
                        category = "Dom"
                    }
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
                price = 22.12
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
                price = 33.13
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
                price = 44.14
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
                price = 55.15
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
                price = 66.16
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
                price = 77.17
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
                price = 88.18
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


