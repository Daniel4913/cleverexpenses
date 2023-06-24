package com.example.cleverex.domain

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class FetchBillUseCase(
    private val repository: BillsRepository,
) {
    suspend fun fetchBill(billId: ObjectId): Flow<RequestState<Bill>> {
      return  repository.getSelectedBill(billId)
    }


}
