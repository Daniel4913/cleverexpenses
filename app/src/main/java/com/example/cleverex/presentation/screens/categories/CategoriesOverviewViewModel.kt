package com.example.cleverex.presentation.screens.categories

import androidx.lifecycle.ViewModel
import com.example.cleverex.displayable.CategoryDisplayable
import com.example.cleverex.domain.category.CategoryEntityToDisplayableMapper
import com.example.cleverex.domain.category.FetchCategoriesUseCase

class CategoriesOverviewViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val displayableMapper: CategoryEntityToDisplayableMapper
) : ViewModel() {

    var categories: List<CategoryDisplayable> = listOf()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        categories = fetchCategoriesUseCase.execute().map {
            displayableMapper.map(it)
        }
    }
}
