package com.example.cleverex

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.data.FakeBillsDb
import com.example.cleverex.domain.FetchBillUseCase
import com.example.cleverex.presentation.displayable.BillToDisplayableMapper
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<BillsRepository> {
//        MongoDB()
        FakeBillsDb()
    }

    single {
        FetchBillUseCase(get())
    }

    viewModel {
        HomeViewModel(billsRepo = get())
    }

    viewModel {
        AddBillViewModel(
            fetchBillUseCase = get(),
            billsRepo = get(),
            savedStateHandle = get(),
        )
    }

    viewModel {
        BillOverviewViewModel(
            fetchBillUseCase = get(),
            savedStateHandle = get(),
            displayableMapper = BillToDisplayableMapper()
        )
    }
}