package com.example.cleverex.domain.home

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.displayable.bill.BillsToByWeeksMapper
import com.example.cleverex.domain.BillsByWeeks

class FetchAllBillsUseCase(
    private val billsRepo: BillsRepository,
    private val mapper: BillsToByWeeksMapper,
) {
    suspend fun execute(): BillsByWeeks {
        return mapper.map(billsRepo.getAllBills())
    }


}