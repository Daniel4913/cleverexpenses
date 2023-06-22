package com.example.cleverex.domain

import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.domain.AllCategoriesDtoToCategoryEntityMapper


class FetchCategoriesUseCase(
    private val repository: CategoriesRepository,
    private val mapper: AllCategoriesDtoToCategoryEntityMapper
) {

    //todo mapowanie z DTO ktore ma w sobie categories: List<CategoryDto>
    fun execute(): List<CategoryEntity> {
        return repository.getCategories().categories.map {mapper.map(it)
        }
    }
}