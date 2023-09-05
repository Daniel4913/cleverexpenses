package com.example.cleverex.presentation.screens.categories

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.CategoriesRepository
import com.example.cleverex.displayable.category.CategoryDisplayable
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.CategoryEntity
import com.example.cleverex.domain.browseCategory.CategoryEntityToCategoryRealmMapper
import com.example.cleverex.domain.browseCategory.CategoryEntityToDisplayableMainMapper
import com.example.cleverex.domain.browseCategory.FetchCategoriesUseCase
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.MainMapper
import com.example.cleverex.domain.browseCategory.Name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId


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
    private val displayableMapper: CategoryEntityToDisplayableMainMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesState())
    val uiState: StateFlow<CategoriesState> = _uiState.asStateFlow()

    private var categoriesTemp: MutableList<CategoryDisplayable> = mutableListOf()

    init {
        fetchCategories()
        fetchCategory()
    }

    fun insertCategory() {
        viewModelScope.launch(Dispatchers.Main) {
            insertCategoryUseCase.insertCategory(
                categoryState = CategoriesState(
                    newCategoryColor = _uiState.value.newCategoryColor,
                    newCategoryName = _uiState.value.newCategoryName,
                    newCategoryIcon = _uiState.value.newCategoryIcon,
                )
            )
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            val categoriesEntities = fetchCategoriesUseCase.fetch()
            _uiState.update { categoriesState ->
                categoriesState.copy(
                    categories = categoriesEntities
                )
            }

        }
    }


    private fun fetchCategory() {
        viewModelScope.launch {
//            fetchCategoryUseCase.execute()

        }
    }

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
    private val mapper: CategoryEntityToCategoryRealmMapper,
    private val toEntity: ToEntityMapper,
) {

    suspend fun insertCategory(categoryState: CategoriesState) {
        val categoryEntity = toEntity.map(categoryState)
        execute(categoryEntity)
    }

    private suspend fun execute(category: CategoryEntity) {
        return repository.insertCategory(mapper.map(category))
    }
}


class ToEntityMapper : MainMapper<CategoriesState, CategoryEntity> {
    override fun map(from: CategoriesState): CategoryEntity {
        return CategoryEntity(
            name = Name(value = from.newCategoryName),
            icon = Icon(value = from.newCategoryIcon),
            categoryColor = CategoryColor(
                value = from.newCategoryColor.value.toString()
            )
        )
    }

}

class FetchCategoryUseCase(private val repository: CategoriesRepository) {
    suspend fun execute(id: ObjectId) {
        return repository.getCategory(id)
    }
}
