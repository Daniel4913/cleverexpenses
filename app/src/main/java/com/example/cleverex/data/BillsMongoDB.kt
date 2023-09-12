package com.example.cleverex.data

import androidx.compose.material3.TimeInput
import com.example.cleverex.domain.Bill
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

class BillsMongoDB(
) : BaseRealmRepository(), BillsRepository {
    init {
        configureTheRealm()
    }


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

    override suspend fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>> {
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
        return if (user != null) {
            realm.write {
                try {
                    val ocrRepo = OcrLogsRepositoryImpl
                    val addedBill = copyToRealm(bill.apply {
                        ownerId = user.id
//                        ocrPositions = ocrRepo.finalOcrLogsList
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
                try {
                    val queriedBill = query<Bill>(query = "_id == $0", bill._id).first().find()
                    if (queriedBill != null) {
                        queriedBill.shop = bill.shop
                        queriedBill.address = bill.address
                        queriedBill.billDate = bill.billDate
                        queriedBill.price = bill.price
                        queriedBill.billItems = bill.billItems
                        queriedBill.billImage = bill.billImage
                        queriedBill.paymentMethod = bill.paymentMethod
//                    queriedBill.ocrPositions = bill.ocrPositions
                        RequestState.Success(data = queriedBill)
                    } else {
                        RequestState.Error(error = Exception("Queried bill does not exist."))
                    }
                } catch (e: Exception) {
                    RequestState.Error(error = Exception("Error with update: $e message:${e.message}."))
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