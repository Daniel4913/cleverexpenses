package com.example.cleverex.domain

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.displayable.BillsByWeeks
import com.example.cleverex.displayable.BillsToByWeeksMapper

class FetchAllBillsUseCase(
    private val billsRepo: BillsRepository,
    private val mapper: BillsToByWeeksMapper,
) {
    suspend fun execute(): BillsByWeeks {
        return mapper.map(billsRepo.getAllBills())
    }
}