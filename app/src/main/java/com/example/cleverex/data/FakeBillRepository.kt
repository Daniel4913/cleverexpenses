package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface FakeBillRepository {

    fun getAllFakeBills(): Flow<List<Bill>>

    fun getSelectedFakeBill(billId: ObjectId): Flow<RequestState<Bill>>

}