package com.example.cleverex.data

import com.example.cleverex.domain.AllCategoriesDto
import com.example.cleverex.model.CategoryItem

interface CategoriesRepository {

    fun getCategories(): AllCategoriesDto
    fun getCategory(): CategoryItem
    fun insertCategory(): CategoryItem
    fun updateCategory(): CategoryItem
    fun deleteCategory(): CategoryItem

}
