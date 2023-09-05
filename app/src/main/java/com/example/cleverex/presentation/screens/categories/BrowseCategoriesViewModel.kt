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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import timber.log.Timber


data class CategoriesState(
    val newCategoryColor: Color = Color.Blue.copy(alpha = 0.5f),
    val newCategoryName: String = "",
    val newCategoryIcon: String = "",
    val colorPickerShowing: Boolean = false,
    val categories: List<CategoryDisplayable> = listOf()
)

class BrowseCategoriesViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val fetchCategoryUseCase: FetchCategoryUseCase,
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val displayableMapper: CategoryEntityToDisplayableMainMapper
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesState())
    val uiState: StateFlow<CategoriesState> = _uiState.asStateFlow()

    private val categoriesTemp: MutableList<CategoryDisplayable> = mutableListOf()

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

    fun createCategory() {
        val newCategory = CategoryDisplayable(
            id = ObjectId.invoke(),
            name = Name(value = _uiState.value.newCategoryName),
            icon = Icon(value = _uiState.value.newCategoryIcon),
            categoryColor = CategoryColor(
                value = _uiState.value.newCategoryColor.value.toString()
            )
        )
        categoriesTemp.add(newCategory)

        _uiState.update { currentState ->
            currentState.copy(
                categories = categoriesTemp
            )
        }
    }

    fun setName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newCategoryName = name
            )
        }
    }

    fun setIcon(icon: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newCategoryIcon = icon
            )
        }
    }

    fun setColor(color: Color) {
        _uiState.update { currentState ->
            currentState.copy(
                newCategoryColor = color
            )
        }
    }

    fun showColorPicker(show: Boolean) {
        if (show) {
            _uiState.update { currentState ->
                currentState.copy(
                    colorPickerShowing = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    colorPickerShowing = false
                )
            }
        }
    }
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
