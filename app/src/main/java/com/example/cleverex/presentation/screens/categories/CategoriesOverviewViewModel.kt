package com.example.cleverex.presentation.screens.categories

import androidx.lifecycle.ViewModel
import com.example.cleverex.displayable.CategoryDisplayable
import com.example.cleverex.domain.CategoryEntityToDisplayableMapper
import com.example.cleverex.domain.FetchCategoriesUseCase
import com.example.cleverex.model.CategoryItem

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
