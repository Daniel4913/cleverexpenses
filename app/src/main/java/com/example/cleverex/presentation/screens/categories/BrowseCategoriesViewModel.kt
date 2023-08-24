package com.example.cleverex.presentation.screens.categories

import androidx.lifecycle.ViewModel
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase

class BrowseCategoriesViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val displayableMapper: CategoryEntityToDisplayableMainMapper
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
