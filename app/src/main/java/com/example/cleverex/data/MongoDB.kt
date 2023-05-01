package com.example.cleverex.data

import android.app.AlertDialog
import android.util.Log
import com.example.cleverex.data.MongoDB.app
import com.example.cleverex.model.Bill
import com.example.cleverex.model.BillItem
import com.example.cleverex.model.Categories
import com.example.cleverex.util.Constants.APP_ID
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.sync.DiscardUnsyncedChangesStrategy
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.lang.IllegalStateException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object MongoDB : MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser


    private lateinit var realm: Realm


    init {
        configureTheRealm2()
    }

    override fun configureTheRealm2() {
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

    override fun getAllBills(): Flow<BillsByWeeks> {
        return if (user != null) {
            try {
                realm.query<Bill>(query = "ownerId == $0", user.id)
                    .sort(property = "billDate", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->

                        val billRealmResults: RealmResults<Bill> = result.list

                        class WeekWithDate(){

                        }

                        RequestState.Success(
                            data = billRealmResults.groupBy {
                                val billInstant = it.billDate.toInstant()
                                val calendar = Calendar.getInstance()

                                calendar.time = Date.from(billInstant)

                                Log.d("WEEK_OF_YEAR", "${calendar.get(Calendar.WEEK_OF_YEAR)}")
                                calendar.get(Calendar.WEEK_OF_YEAR)

//                                it.billDate.toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
                            }

                        )


//                        RequestState.Success(
//                            data = result.list.groupBy {
//                                it.billDate.toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
//                            }
//                        )
                    }
            } catch (e: java.lang.Exception) {
                flow { emit(RequestState.Error(e)) }
            }

        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>> {
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
                    val addedBill = copyToRealm(bill.apply {
                        ownerId = user.id
                    })
                    RequestState.Success(data = addedBill)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
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