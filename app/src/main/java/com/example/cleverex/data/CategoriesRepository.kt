package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.AllCategoriesDto
import com.example.cleverex.domain.browseCategory.Category

interface CategoriesRepository {

    fun getCategories(): AllCategoriesDto
    fun getCategory(): Category
    fun insertCategory(): Category
    fun updateCategory(): Category
    fun deleteCategory(): Category

}
