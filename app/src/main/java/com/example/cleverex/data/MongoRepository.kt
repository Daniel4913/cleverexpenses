package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime

typealias Bills = RequestState<Map<LocalDate, List<Bill>>>
typealias BillsByWeeks = RequestState<Map<Int, List<Bill>>>

interface MongoRepository {
    fun configureTheRealm2() {}
    fun getAllBills(): Flow<BillsByWeeks>
    fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>>
    suspend fun insertNewBill(bill: Bill): RequestState<Bill>
    suspend fun updateBill(bill: Bill): RequestState<Bill>
    suspend fun deleteBill(id: ObjectId): RequestState<Bill>

}

