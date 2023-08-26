package com.example.cleverex.data

import com.example.cleverex.domain.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Bills = RequestState<Map<LocalDate, List<Bill>>>


interface BillsRepository {
    suspend fun getAllBills(): Flow<RequestState<List<Bill>>>

    suspend fun getBillAndBillItems(billId: ObjectId): Flow<RequestState<Bill>>
    suspend fun getSelectedBill(billId: ObjectId): Flow<RequestState<Bill>>
    suspend fun insertNewBill(bill: Bill): RequestState<Bill>
    suspend fun updateBill(bill: Bill): RequestState<Bill>
    suspend fun deleteBill(id: ObjectId): RequestState<Bill>

//    suspend fun getItem()
}