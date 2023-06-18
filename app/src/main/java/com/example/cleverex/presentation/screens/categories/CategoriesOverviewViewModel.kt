package com.example.cleverex.presentation.screens.categories

import androidx.lifecycle.ViewModel
import com.example.cleverex.domain.FetchCategoriesUseCase
import com.example.cleverex.model.CategoryItem

class CategoriesOverviewViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase
):ViewModel() {

    var categories: List<CategoryItem> = listOf()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
     categories = fetchCategoriesUseCase.execute()
    }
}
