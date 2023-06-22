package com.example.cleverex

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.data.FakeBillsDb
import com.example.cleverex.data.FakeCategoriesDb
import com.example.cleverex.domain.AllCategoriesDtoToCategoryEntityMapper
import com.example.cleverex.domain.CategoryDtoToEntityMapper
import com.example.cleverex.domain.CategoryEntityToDisplayableMapper
import com.example.cleverex.domain.CreateCategoryUseCase
import com.example.cleverex.domain.FetchAllBillsUseCase
import com.example.cleverex.domain.FetchBillUseCase
import com.example.cleverex.domain.FetchCategoriesUseCase
import com.example.cleverex.presentation.displayable.BillToDisplayableMapper
import com.example.cleverex.presentation.displayable.BillsToByWeeksMapper
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.categories.CategoriesOverviewViewModel
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<BillsRepository> {
//        MongoDB()
        FakeBillsDb()
    }

    single<CategoriesRepository>{
        FakeCategoriesDb()
    }


    single {
        FetchBillUseCase(get())
    }

    single {
        FetchAllBillsUseCase(
            billsRepo = get(),
            mapper = BillsToByWeeksMapper()
        )
    }

    single {
        CategoryDtoToEntityMapper()
    }
    single {
        FetchCategoriesUseCase(
            repository = get(),
            mapper = AllCategoriesDtoToCategoryEntityMapper()
        )
    }

    single {
        CreateCategoryUseCase()
    }



    viewModel {
        HomeViewModel(fetchAllBillsUseCase = get())
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

    viewModel {
        CategoriesOverviewViewModel(
            fetchCategoriesUseCase = get(),
            displayableMapper = get()
        )
    }

    single {
        CategoryEntityToDisplayableMapper()
    }
}