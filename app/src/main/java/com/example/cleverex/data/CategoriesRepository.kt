package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.CategoryEntity
import com.example.cleverex.domain.browseCategory.CategoryEmbedded
import com.example.cleverex.domain.browseCategory.CategoryRealm
import org.mongodb.kbson.ObjectId


interface CategoriesRepository {
    suspend fun getCategories(): List<CategoryRealm>
    suspend fun getCategory(categoryId: ObjectId)
    //    fun getCategory(): CategoryEntity

    suspend fun insertCategory(category: CategoryRealm)
    suspend fun updateCategory(category: CategoryRealm)
    suspend fun deleteCategory()

}
