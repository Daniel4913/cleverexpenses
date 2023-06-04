package com.example.cleverex

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.data.FakeBillsDb
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<BillsRepository> {
//        MongoDB()
        FakeBillsDb()
    }

    viewModel {
        HomeViewModel(billsRepo = get())
    }

    viewModel {
        AddBillViewModel(
            billsRepo = get(),
            savedStateHandle = get()
        )
    }
}