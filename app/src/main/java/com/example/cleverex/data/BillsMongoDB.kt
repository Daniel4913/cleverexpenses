package com.example.cleverex.data

import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.util.RequestState
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import timber.log.Timber

class BillsMongoDB : BaseRealmRepository(), BillsRepository {



    //TODO BaseRealmRepository() daje mi dostęp do user i realm, ale jest to nieintuicyjne więc
    // chcialbym to wyciągnąć tutaj
//    val user = BaseRealmRepository().user ALE toto nie dziaua
    init {
        configureTheRealm()
    }

//    override suspend fun getItem() {
//        Timber.d("fetching Item 64e8b6d016758357542d14dd")
//        val ajdi = ObjectId("64e8b6d016758357542d14dd")
//        val billItemFlow = realm.query<BillItem>("_id == $0", ajdi).asFlow()
//        billItemFlow.collect { billItems ->
//            Timber.d(".collect { ${billItems.list}")
//            val billItem = billItems.list.firstOrNull()
//            if (billItem != null) {
//                // Tutaj możesz zrobić coś z billItem
//                Timber.d("Znaleziono BillItem: $billItem")
//
//            } else {
//                Timber.d("Nie znaleziono BillItem o ID: $ajdi")
//            }
//        }
//    }


    override suspend fun getAllBills(): Flow<RequestState<List<Bill>>> {
        return flow {
            if (user != null) {
                val results = realm.query<Bill>("ownerId == '${user.id}'")
                    .sort("billDate", Sort.DESCENDING)
//                        .findAll()
                    .asFlow()
                    .first() // Pobierz pierwszy zestaw wyników
                emit(RequestState.Success(data = results.list))
            } else {
                Timber.d("User not authenticated????")
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }.catch { e ->
            Timber.d("catch block: $e message: ${e.message}")
            emit(RequestState.Error(e))

        }
    }

    override suspend fun getBillAndBillItems(billId: ObjectId): Flow<RequestState<Bill>> {
        return flow {
            if (user != null) {
                try {
                    val results = realm.query<Bill>("_id == $0", billId).asFlow().first()
                    when (results) {
                        is InitialResults<Bill> -> {
                            val bill = results.list.first()
                            // Teraz możesz uzyskać dostęp do billItems
                            val billItems =
                                bill.billItems // Zakładając, że masz właściwość billItems w klasie Bill

                            emit(RequestState.Success(data = bill))
                        }

                        else -> {
                            // Obsłuż inne przypadki, jeśli to konieczne
                            // ...
                        }
                    }
                } catch (e: Exception) {
                    emit(RequestState.Error(e))
                }
            } else {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }


//    override suspend fun getAllBills(): Flow<RequestState<List<Bill>>> {
//        return if (user != null) {
//            try {
//                realm.query<Bill>(query = "ownerId == $0", user.id)
//                    .sort(property = "billDate", sortOrder = Sort.DESCENDING)
//                    .asFlow()
//                    .map { result ->
////                        val billRealmResults: RealmResults<Bill> = result.list
//                        Timber.d(">>>>>NEW DATA IN REALM")
//                       RequestState.Success(
//                          data= result.list.toList())
//                    }
//            } catch (e: java.lang.Exception) {
//                Timber.d("catch block: $e message: ${e.message}")
////                flow { emit(emptyList<Bill>()) }
//                flow { emit(RequestState.Error(e)) }
//            }
//
//        } else {
//            Timber.d("User not authenticated????")
////            flow { emit(emptyList<Bill>()) }
//            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
//        }
//    }

//    override suspend fun getAllBills(): List<Bill> {
//        TODO("Not yet implemented")
//    }

    //tu nizej stare
//    override fun getAllBills(): Flow<BillsByWeeks> {
//        return if (user != null) {
//            try {
//                realm.query<Bill>(query = "ownerId == $0", user.id)
//                    .sort(property = "billDate", sortOrder = Sort.DESCENDING)
//                    .asFlow()
//                    .map { result ->
//                        val billRealmResults: RealmResults<Bill> = result.list
//
//                        class WeekWithDate(){
//
//                        }
//
//                        RequestState.Success(
//                            data = billRealmResults.groupBy {
//                                val billInstant = it.billDate.toInstant()
//                                val calendar = Calendar.getInstance()
//
//                                calendar.time = Date.from(billInstant)
//                                Log.d("WEEK_OF_YEAR", "${calendar.get(Calendar.WEEK_OF_YEAR)}")
//                                calendar.get(Calendar.WEEK_OF_YEAR)
//                            })
//
////                        RequestState.Success(
////                            data = result.list.groupBy {
////                                it.billDate.toInstant()
////                                    .atZone(ZoneId.systemDefault())
////                                    .toLocalDate()
////                            })
//                    }
//            } catch (e: java.lang.Exception) {
//                flow { emit(RequestState.Error(e)) }
//            }
//
//        } else {
//            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
//        }
//    }

    override suspend fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>> {
        Timber.d("getSelectedBill invoked")
        return if (user != null) {
            try {
                realm.query<Bill>("_id == $0", billId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }


    override suspend fun insertNewBill(bill: Bill): RequestState<Bill> {
        Timber.d("$user")
        return if (user != null) {
            realm.write {
                try {
                    Timber.d("$bill")
                    val addedBill = copyToRealm(bill.apply {
                        ownerId = user.id

                    })
                    RequestState.Success(data = addedBill)
                } catch (e: Exception) {
                    Timber.d("$e , ${e.message}")
                    RequestState.Error(e)
                }
            }
        } else {
            Timber.d("UserNotAuthenticatedException")
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateBill(bill: Bill): RequestState<Bill> {
        return if (user != null) {
            realm.write {
                val queriedBill = query<Bill>(query = "_id == $0", bill._id).first().find()
                if (queriedBill != null) {
                    queriedBill.shop = bill.shop
                    queriedBill.address = bill.address
                    queriedBill.billDate = bill.billDate
                    queriedBill.price = bill.price
                    queriedBill.billItems = bill.billItems
                    queriedBill.billImage = bill.billImage
                    queriedBill.billTranscription = bill.billTranscription
                    RequestState.Success(data = queriedBill)
                } else {
                    RequestState.Error(error = Exception("Queried bill does not exist."))
                }

            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteBill(id: ObjectId): RequestState<Bill> {
        return if (user != null) {
            realm.write {
                val bill = query<Bill>(query = "_id == $0 AND ownerId ==$1", id, user.id)
                    .first().find()
                if (bill != null) {
                    try {
                        delete(bill)
                        RequestState.Success(data = bill)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(Exception("Bill does not exist"))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    private class UserNotAuthenticatedException : Exception("User is not Logged in.")
}