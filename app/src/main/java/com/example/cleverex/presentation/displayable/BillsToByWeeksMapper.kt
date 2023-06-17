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

typealias BillsByWeeks = Map<Int, List<Bill>>

class BillsToByWeeksMapper: Mapper<List<Bill>,BillsByWeeks> {

    override fun map(from: List<Bill>): BillsByWeeks {

     return from.groupBy {
          val billInstant =   it.billDate.toInstant()
            val calendar = Calendar.getInstance()

            calendar.time =Date.from(billInstant)
            calendar.get(Calendar.WEEK_OF_YEAR)
        }

//            from.map { result: Bill ->
//                val billRealmResults: RealmResults<Bill> = result.list
//
//                RequestState.Success(
//                    data = billRealmResults.groupBy {
//                        val billInstant = it.billDate.toInstant()
//                        val calendar = Calendar.getInstance()
//
//                        calendar.time = Date.from(billInstant)
//                        Log.d("WEEK_OF_YEAR", "${calendar.get(Calendar.WEEK_OF_YEAR)}")
//                        calendar.get(Calendar.WEEK_OF_YEAR)
//                    })
//
//                        RequestState.Success(
//                            data = result.list.groupBy {
//                                it.billDate.toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
//                            })
//            }
    }




}