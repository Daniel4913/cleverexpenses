package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.CategoryEntity


interface CategoriesRepository {
    fun getCategories(): List<CategoryEntity>
    fun getCategory(): CategoryEntity
    fun insertCategory(): CategoryEntity
    fun updateCategory(): CategoryEntity
    fun deleteCategory(): CategoryEntity

}
