package com.example.cleverex.presentation.displayable

import android.util.Log
import com.example.cleverex.data.Bills
import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toInstant
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date

typealias BillsByWeeks = RequestState<Map<Int, List<Bill>>>

//class BillToByWeeksMapper: Mapper<Bills,BillsByWeeks> {
//    override fun map(from: Bills): BillsByWeeks {
//
////        realm.query<Bill>(query = "ownerId == $0", user.id)
////            .sort(property = "billDate", sortOrder = Sort.DESCENDING)
////            .asFlow()
////            .map { result ->
////                val billRealmResults: RealmResults<Bill> = result.list
////
////                class WeekWithDate(){
////
////                }
////
////                RequestState.Success(
////                    data = billRealmResults.groupBy {
////                        val billInstant = it.billDate.toInstant()
////                        val calendar = Calendar.getInstance()
////
////                        calendar.time = Date.from(billInstant)
////                        Log.d("WEEK_OF_YEAR", "${calendar.get(Calendar.WEEK_OF_YEAR)}")
////                        calendar.get(Calendar.WEEK_OF_YEAR)
////                    })
////
//////                        RequestState.Success(
//////                            data = result.list.groupBy {
//////                                it.billDate.toInstant()
//////                                    .atZone(ZoneId.systemDefault())
//////                                    .toLocalDate()
//////                            })
////            }
////
//        return BillsByWeeks(
//
//        )
//    }
//
//
//}