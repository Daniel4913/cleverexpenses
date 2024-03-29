package com.example.cleverex.domain.browseCategory

import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.displayable.category.CategoryDisplayable


class FetchCategoriesUseCase(
    private val repository: CategoriesRepository,
    private val mapper: ListCategoryRealmToListEntityMapper,
    private val toListDisplayable: ListCategoryEntityToListDisplayableMapper
) {

    suspend fun fetch(): List<CategoryDisplayable> {
        return toListDisplayable.map(execute())
    }

    private suspend fun execute(): List<CategoryEntity> {
        return mapper.map(repository.getCategories())
    }

}

