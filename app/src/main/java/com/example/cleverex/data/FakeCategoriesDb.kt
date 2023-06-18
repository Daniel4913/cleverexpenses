package com.example.cleverex.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.ui.graphics.Color
import com.example.cleverex.domain.AllCategoriesDto
import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.CategoryItem
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name

class FakeCategoriesDb : CategoriesRepository {

    override fun getCategories(): AllCategoriesDto {
        TODO("Not yet implemented")
    }

    override fun getCategory(): CategoryItem {
        TODO("Not yet implemented")
    }

    override fun insertCategory(): CategoryItem {
        TODO("Not yet implemented")
    }

    override fun updateCategory(): CategoryItem {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(): CategoryItem {
        TODO("Not yet implemented")
    }

    private val fakeCategories =
        listOf(
            CategoryItem(
                name = Name(value = "House"),
                icon = Icon(value = Icons.Rounded.Home),
                categoryColor = CategoryColor(value = Color.Green)
            ),
            CategoryItem(
                name = Name(value = "Cleaning products"),
                icon = Icon(value = Icons.Rounded.Clear),
                categoryColor = CategoryColor(value = Color.Blue)
            ),
            CategoryItem(
                name = Name(value = "Food"),
                icon = Icon(value = Icons.Rounded.Clear),
                categoryColor = CategoryColor(value = Color.Blue)
            ),
            CategoryItem(
                name = Name(value = "Street Food"),
                icon = Icon(value = Icons.Rounded.Clear),
                categoryColor = CategoryColor(value = Color.Blue)
            ),
            CategoryItem(
                name = Name(value = "Electronics"),
                icon = Icon(value = Icons.Rounded.Phone),
                categoryColor = CategoryColor(value = Color.Blue)
            ),
            CategoryItem(
                name = Name(value = "Electronics"),
                icon = Icon(value = Icons.Rounded.Phone),
                categoryColor = CategoryColor(value = Color.Blue)
            )
        )
}