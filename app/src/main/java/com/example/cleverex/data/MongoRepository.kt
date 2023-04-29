package com.example.cleverex.data

import androidx.compose.material3.DateRangePicker
import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Bills = RequestState<Map<LocalDate, List<Bill>>>

interface MongoRepository {
    fun configureTheRealm() {}
    fun getAllBills(): Flow<Bills>
    fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>>
    suspend fun insertNewBill(bill: Bill): RequestState<Bill>
    suspend fun updateBill(bill: Bill): RequestState<Bill>
    suspend fun deleteBill(id: ObjectId): RequestState<Bill>

}

object DateRange {

}

