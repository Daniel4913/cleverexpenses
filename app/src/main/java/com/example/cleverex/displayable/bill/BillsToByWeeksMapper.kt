package com.example.cleverex.displayable.bill

import com.example.cleverex.domain.BillsByWeeks
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.browseCategory.FlowMapper
import com.example.cleverex.util.RequestState
import com.example.cleverex.util.toInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

class BillsToByWeeksMapper : FlowMapper<List<Bill>, BillsByWeeks> {

//    override fun map(from: List<Bill>): BillsByWeeks { //na fejkowych

    override suspend fun map(from: Flow<RequestState<List<Bill>>>): BillsByWeeks {
        val requestState = from.first()
        if (requestState is RequestState.Success) {
            return requestState.data.groupBy {
                val billInstant = it.billDate.toInstant()
                val calendar = Calendar.getInstance()

                calendar.time = Date.from(billInstant)
                calendar.get(Calendar.WEEK_OF_YEAR)
            }
        } else {

            return emptyMap()

        }
    }


    //gpt ftw: Twoja funkcja map próbuje zwrócić wynik funkcji collect, która zwraca Unit. Dlatego otrzymujesz ten błąd. Aby naprawić to, musisz użyć funkcji toList na obiekcie Flow i następnie użyć wyniku tej operacji do wykonania grupowania. Oto poprawiona funkcja:
//    override suspend fun map(from: Flow<RequestState<List<Bill>>>): BillsByWeeks {
//        return from.first().groupBy {
//            val billInstant = it.billDate.toInstant()
//            val calendar = Calendar.getInstance()
//
//            calendar.time = Date.from(billInstant)
//            calendar.get(Calendar.WEEK_OF_YEAR)
//        }
//    }
    //gpt: W powyższym kodzie używam funkcji first na obiekcie Flow, aby pobrać pierwszy element (w tym przypadku listę Bill) i następnie używam funkcji groupBy na tej liście.

//   return from.collect{
//        it.groupBy {
//            val billInstant =   it.billDate.toInstant()
//            val calendar = Calendar.getInstance()
//
//            calendar.time = Date.from(billInstant)
//            calendar.get(Calendar.WEEK_OF_YEAR)
//        }
//    }
//
//     return from.groupBy {
//          val billInstant =   it.billDate.toInstant()
//            val calendar = Calendar.getInstance()
//
//            calendar.time = Date.from(billInstant)
//            calendar.get(Calendar.WEEK_OF_YEAR)
//        }

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




