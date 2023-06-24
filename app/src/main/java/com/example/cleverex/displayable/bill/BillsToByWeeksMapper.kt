package com.example.cleverex.displayable.bill

import com.example.cleverex.domain.BillsByWeeks
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.browseCategory.Mapper
import com.example.cleverex.util.toInstant
import java.util.Calendar
import java.util.Date

class BillsToByWeeksMapper: Mapper<List<Bill>, BillsByWeeks> {

    override fun map(from: List<Bill>): BillsByWeeks {

     return from.groupBy {
          val billInstant =   it.billDate.toInstant()
            val calendar = Calendar.getInstance()

            calendar.time = Date.from(billInstant)
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