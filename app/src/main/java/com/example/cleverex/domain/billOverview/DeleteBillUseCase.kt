package com.example.cleverex.domain.billOverview

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class DeleteBillUseCase(
    private val billsRepository: BillsRepository
) {
    suspend fun execute(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result =
            billsRepository.deleteBill(id = ObjectId.invoke(id))
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }
    }
}
