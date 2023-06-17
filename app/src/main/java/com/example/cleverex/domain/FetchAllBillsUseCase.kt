package com.example.cleverex.domain

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.model.Bill
import com.example.cleverex.presentation.displayable.BillsByWeeks
import com.example.cleverex.presentation.displayable.BillsToByWeeksMapper
import kotlinx.coroutines.flow.Flow

class FetchAllBillsUseCase(
    private val billsRepo: BillsRepository,
    private val mapper: BillsToByWeeksMapper,
) {
    suspend fun execute(): BillsByWeeks {
        return mapper.map(billsRepo.getAllBills())
    }
}