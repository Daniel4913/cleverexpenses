package com.example.cleverex

import com.example.cleverex.data.BillsRepository

import com.example.cleverex.data.BillsMongoDB
import com.example.cleverex.data.CategoriesMongoDb
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.data.datastore.BudgetDataStore
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.CreateCategoryUseCase
import com.example.cleverex.domain.billOverview.FetchAllBillsUseCase
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.displayable.bill.BillToDisplayableMainMapper
import com.example.cleverex.displayable.bill.BillsToByWeeksMapper
import com.example.cleverex.domain.browseCategory.CategoryEntityToCategoryRealmMapper
import com.example.cleverex.domain.browseCategory.ListCategoryEntityToListDisplayableMapper
import com.example.cleverex.domain.browseCategory.ListCategoryRealmToListEntityMapper
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.billOverview.FetchItemUseCase
import com.example.cleverex.presentation.screens.budget.BudgetViewModel
import com.example.cleverex.presentation.screens.categories.BrowseCategoriesViewModel
import com.example.cleverex.presentation.screens.categories.FetchCategoryUseCase
import com.example.cleverex.presentation.screens.categories.InsertCategoryUseCase
import com.example.cleverex.presentation.screens.categories.ToEntityMapper
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
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
            repository = get(),
            mapper = CategoryEntityToCategoryRealmMapper(),
            toEntity = get()
        )
    }

    single {
        ToEntityMapper()
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
        CategoryEntityToCategoryRealmMapper()
    }

    single {
        FetchCategoryUseCase(repository = get())
    }

    single {
        FetchCategoriesUseCase(
            repository = get(),
            mapper = get(),
            toListDisplayable = get(),

            )
    }

    single {
        ListCategoryEntityToListDisplayableMapper()
    }

    single {
        CreateCategoryUseCase()
    }

    single {
        ListCategoryRealmToListEntityMapper()
    }

    single {
        CategoryEntityToDisplayableMainMapper()
    }

//    single {
//        OcrLogsRepositoryImpl()
//    }

    single {
        BudgetDataStore(context = androidApplication())
    }

    viewModel {
        BudgetViewModel(budgetDataStore = get())
    }

    viewModel {
        HomeViewModel(fetchAllBillsUseCase = get(), budgetDataStore = get())
    }

    viewModel {
        AddBillViewModel(
            fetchBillUseCase = get(),
            billsRepo = get(),
            savedStateHandle = get(),
            fetchCategoriesUseCase = get()
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