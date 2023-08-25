package com.example.cleverex.domain.browseCategory

import com.example.cleverex.data.CategoriesRepository


class FetchCategoriesUseCase(
    private val repository: CategoriesRepository,
) {
    fun execute(): List<CategoryEntity> {
        return repository.getCategories()
    }
}
