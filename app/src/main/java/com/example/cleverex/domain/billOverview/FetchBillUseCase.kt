package com.example.cleverex.domain.billOverview

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import timber.log.Timber

class FetchBillUseCase(
    private val repository: BillsRepository,
) {
    suspend fun fetchBill(billId: ObjectId): Flow<RequestState<Bill>> {
        return repository.getBillAndBillItems(billId)
//        return repository.getSelectedBill(billId)
    }
}
