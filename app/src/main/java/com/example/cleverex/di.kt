package com.example.cleverex

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.data.FakeCategoriesDb
import com.example.cleverex.data.MongoDB
import com.example.cleverex.domain.browseCategory.AllCategoriesDtoToCategoryEntityMainMapper
import com.example.cleverex.domain.browseCategory.CategoryDtoToEntityMainMapper
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.CreateCategoryUseCase
import com.example.cleverex.domain.billOverview.FetchAllBillsUseCase
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.displayable.bill.BillToDisplayableMainMapper
import com.example.cleverex.displayable.bill.BillsToByWeeksMapper
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.addItems.AddItemsViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.categories.BrowseCategoriesViewModel
import com.example.cleverex.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<BillsRepository> {
        MongoDB()
//        FakeBillsDb()
    }

    single<CategoriesRepository> {
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
        CategoryDtoToEntityMainMapper()
    }
    single {
        FetchCategoriesUseCase(
            repository = get(),
            mapper = AllCategoriesDtoToCategoryEntityMainMapper()
        )
    }

    single {
        CreateCategoryUseCase()
    }


    viewModel {
        AddItemsViewModel()
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
            displayableMapper = BillToDisplayableMainMapper()
        )
    }

    viewModel {
        BrowseCategoriesViewModel(
            fetchCategoriesUseCase = get(),
            displayableMapper = get()
        )
    }

    single {
        CategoryEntityToDisplayableMainMapper()
    }
}