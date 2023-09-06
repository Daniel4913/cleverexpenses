package com.example.cleverex.domain.browseCategory

import com.example.cleverex.data.CategoriesRepository
import org.mongodb.kbson.ObjectId

class DeleteCategoryUseCase(
    private val repository: CategoriesRepository
) {

    suspend fun deleteCategory(id: ObjectId) {
        repository.deleteCategory(id)
    }
}