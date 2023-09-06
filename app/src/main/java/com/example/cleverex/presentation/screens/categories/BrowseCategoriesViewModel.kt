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
import com.example.cleverex.domain.browseCategory.DeleteCategoryUseCase
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
import timber.log.Timber


data class CategoriesState(
    val categoryId: ObjectId? = null,
    val newCategoryColor: Color = Color.Blue.copy(alpha = 0.5f),
    val newCategoryName: String = "",
    val newCategoryIcon: String = "",
    val colorPickerShowing: Boolean = false,
    val categories: List<CategoryDisplayable> = listOf(),
    val pickedCategory: CategoryTemp = CategoryTemp(),
    val selectedCategory: MutableList<CategoryDisplayable> = mutableListOf()
) {
    data class CategoryTemp(
        val name: String = "",
        val icon: String = "",
        val color: String = "",
    )
}

class BrowseCategoriesViewModel(
    private val fetchCategoriesUseCase: FetchCategoriesUseCase,
    private val fetchCategoryUseCase: FetchCategoryUseCase,
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val displayableMapper: CategoryEntityToDisplayableMainMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesState())
    val uiState: StateFlow<CategoriesState> = _uiState.asStateFlow()

    private var categoriesTemp: MutableList<CategoryDisplayable> = mutableListOf()

    init {
        fetchCategories()
        fetchCategory()
    }


    fun toggleSelectedCategory(categoryId: ObjectId, isPicked: Boolean) {
        val pickedCategory =
            _uiState.value.categories.find { it.id == categoryId }
        val selectedCategory =
            _uiState.value.categories.toMutableList()
        selectedCategory.forEach { it.categoryPicked = false }

        if (pickedCategory != null) {
            pickedCategory.categoryPicked = isPicked
            selectedCategory.clear()
            selectedCategory.add(pickedCategory)
            if (isPicked) {
                _uiState.value.selectedCategory.add(pickedCategory)
                setId(pickedCategory.id!!)
                setName(pickedCategory.name.value)
                setIcon(pickedCategory.icon.value)
                setColor(Color(pickedCategory.categoryColor.value.toULong()))
                Timber.d("${_uiState.value.categoryId}")
            }
        }
    }


    fun upsertCategory() {
        viewModelScope.launch(Dispatchers.Main) {
            insertCategoryUseCase.upsertCategory(
                categoryState = CategoriesState(
                    //TODO czy ja muszęm tutaj tworzyć nowe category state?
                    categoryId = _uiState.value.categoryId,
                    newCategoryColor = _uiState.value.newCategoryColor,
                    newCategoryName = _uiState.value.newCategoryName,
                    newCategoryIcon = _uiState.value.newCategoryIcon,
                )
            )
        }
    }

    fun deleteCategory(id: ObjectId) {
        viewModelScope.launch {

            deleteCategoryUseCase.deleteCategory(id)
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

    private fun setId(id: ObjectId) {
        _uiState.update { currentState ->
            currentState.copy(
                categoryId = id
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
    private val toRealmMapper: CategoryEntityToCategoryRealmMapper,
    private val toEntityMapper: CategoriesStateToEntityMapper,
) {

    suspend fun upsertCategory(categoryState: CategoriesState) {
        val categoryEntity = toEntityMapper.map(categoryState)
        if (categoryState.categoryId != null) {
            executeUpdate(categoryEntity)
        } else {
            executeInsert(categoryEntity)
        }
    }

    private suspend fun executeUpdate(category: CategoryEntity) {
        return repository.updateCategory(toRealmMapper.map(category))
    }

    private suspend fun executeInsert(category: CategoryEntity) {
        return repository.insertCategory(toRealmMapper.map(category))
    }


}


class CategoriesStateToEntityMapper : MainMapper<CategoriesState, CategoryEntity> {
    override fun map(from: CategoriesState): CategoryEntity {
        Timber.d("${from.categoryId}")
        return CategoryEntity(
            _id = from.categoryId,
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
