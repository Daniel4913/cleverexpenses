package com.example.cleverex

import com.example.cleverex.data.BillsRepository

import com.example.cleverex.data.BillsMongoDB
import com.example.cleverex.data.CategoriesMongoDb
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.data.OcrLogsRepositoryImpl
import com.example.cleverex.domain.browseCategory.CategoryDtoToEntityMainMapper
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.CreateCategoryUseCase
import com.example.cleverex.domain.billOverview.FetchAllBillsUseCase
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.displayable.bill.BillToDisplayableMainMapper
import com.example.cleverex.displayable.bill.BillsToByWeeksMapper
import com.example.cleverex.domain.addItems.InsertItemsUseCase
import com.example.cleverex.domain.browseCategory.CategoryEntityToCategoryRealmMapper
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.billOverview.FetchItemUseCase
import com.example.cleverex.presentation.screens.budget.BudgetViewModel
import com.example.cleverex.presentation.screens.categories.BrowseCategoriesViewModel
import com.example.cleverex.presentation.screens.categories.FetchCategoryUseCase
import com.example.cleverex.presentation.screens.categories.InsertCategoryUseCase
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//import com.example.cleverex.data.FakeCategoriesDb

val appModule = module {

    single<BillsRepository> {
        BillsMongoDB()
//        FakeBillsDb()
    }

//    single<Unit> {
//        CategoriesMongoDb() //Type mismatch.Required: CategoriesRepository Found: Unit
//    }

    single<CategoriesRepository> {
        CategoriesMongoDb() //Type mismatch.Required: CategoriesRepository Found: Unit
//        FakeCategoriesDb()
    }

    single {
        InsertCategoryUseCase(
            repository = get(), mapper = CategoryEntityToCategoryRealmMapper()

        )
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
        FetchItemUseCase(
            repository = get()
        )
    }

    single {
        CategoryDtoToEntityMainMapper()
    }

    single {
        FetchCategoryUseCase(repository = get())
    }

    single {
        FetchCategoriesUseCase(
            repository = get(),
        )
    }

    single {
        CreateCategoryUseCase()
    }

    single {
        InsertItemsUseCase()
    }

    single {
        CategoryEntityToDisplayableMainMapper()
    }

//    single {
//        OcrLogsRepositoryImpl()
//    }

    viewModel {
        BudgetViewModel()
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
            displayableMapper = BillToDisplayableMainMapper(),
            fetchItemUseCase = get()
        )
    }

    viewModel {
        BrowseCategoriesViewModel(
            fetchCategoriesUseCase = get(),
            fetchCategoryUseCase = get(),
            displayableMapper = get(),
            insertCategoryUseCase = get(),
        )
    }
}