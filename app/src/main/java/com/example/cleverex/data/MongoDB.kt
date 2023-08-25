package com.example.cleverex.data

import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.util.Constants.APP_ID
import com.example.cleverex.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import timber.log.Timber

class MongoDB : BillsRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm


    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        Timber.d("configureTheRealm invoked")
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user,
                setOf(
                    Bill::class,
                    BillItem::class
                )
            ).initialSubscriptions { sub ->
                add(
                    query = sub.query<Bill>("ownerId == $0", user.id),
                    name = "User's Bill's" // optional - name of subscription
//                    query= sub.query("ownerId == $0 AND _id == $1", user.id, Bill._id)
                )
            }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)

        }
    }

    override suspend fun getAllBills(): Flow<RequestState<List<Bill>>> {
        return flow {
            if (user != null) {
                    val results = realm.query<Bill>("ownerId == '${user.id}'")
                        .sort("billDate", Sort.DESCENDING)
//                        .findAll()
                        .asFlow()
                        .first() // Pobierz pierwszy zestaw wyników
                    Timber.d(">>>>>NEW DATA IN REALM")
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