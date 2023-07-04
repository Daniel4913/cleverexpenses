package com.example.cleverex.domain.home

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.lang.Exception

class FetchBillUseCase(
    private val repository: BillsRepository,
) {
    suspend fun fetchBill(billId: ObjectId): Flow<RequestState<Bill>>? {
        return repository.getSelectedBill(billId) ?: throw Exception()
    }

}