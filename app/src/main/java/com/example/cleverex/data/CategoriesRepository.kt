package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.AllCategoriesDto
import com.example.cleverex.domain.browseCategory.CategoryItem

interface CategoriesRepository {

    fun getCategories(): AllCategoriesDto
    fun getCategory(): CategoryItem
    fun insertCategory(): CategoryItem
    fun updateCategory(): CategoryItem
    fun deleteCategory(): CategoryItem

}
