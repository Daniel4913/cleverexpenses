package com.example.cleverex.presentation.screens.categories

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.CategoryEntity
import com.example.cleverex.domain.browseCategory.CategoryEntityToCategoryRealmMapper
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.Name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BrowseCategoriesViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val fetchCategoryUseCase: FetchCategoryUseCase,
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val displayableMapper: CategoryEntityToDisplayableMainMapper
) : ViewModel() {

    var categories: List<CategoryDisplayable> = listOf()

    init {
//        fetchCategories()
//        fetchCategory()
//        insertCategory(
//            CategoryEntity(
//                name = Name(value = "Napoje"),
//                icon = Icon(value = "ikona_napoje"),
//                categoryColor = CategoryColor(value = 0xFF00FF00)
//            )
//        )
    }

//    private fun insertCategory(category: CategoryEntity) {
//        viewModelScope.launch(Dispatchers.Main) {
//            insertCategoryUseCase.insert(category = category)
//        }
//    }

//    private suspend fun fetchCategories() {
//        categories = fetchCategoriesUseCase.execute().map {
//            displayableMapper.map(it)
//        }
//    }


//    private fun fetchCategory() {
//        viewModelScope.launch {
//            fetchCategoryUseCase.execute()
//
//        }
//    }
}

class InsertCategoryUseCase(
    private val repository: CategoriesRepository,
    private val mapper: CategoryEntityToCategoryRealmMapper
) {
//    suspend fun insert(category: CategoryEntity) {
//        return repository.insertCategory(mapper.map(category))
//    }

}

class FetchCategoryUseCase(private val repository: CategoriesRepository) {
//    suspend fun execute() {
//        return repository.getCategory()
//    }

}
