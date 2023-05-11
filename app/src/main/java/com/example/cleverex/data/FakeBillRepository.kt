package com.example.cleverex.data

import com.example.cleverex.model.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface FakeBillRepository {

    fun getAllFakeBills(): Flow<Bills>

    fun getSelectedFakeBill(billId: ObjectId): Flow<RequestState<Bill>>

}