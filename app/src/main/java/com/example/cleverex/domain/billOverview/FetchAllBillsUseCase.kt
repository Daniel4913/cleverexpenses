package com.example.cleverex.domain.billOverview

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.BillsByWeeks
import com.example.cleverex.displayable.bill.BillsToByWeeksMapper

class FetchAllBillsUseCase(
    private val billsRepo: BillsRepository,
    private val mapper: BillsToByWeeksMapper,
) {
    suspend fun execute(): BillsByWeeks {
        return mapper.map(billsRepo.getAllBills())
    }
}