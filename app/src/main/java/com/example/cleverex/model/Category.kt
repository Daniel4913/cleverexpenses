package com.example.cleverex.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface Category {
    fun create(name: Name, icon: Icon, categoryColor: CategoryColor): CategoryItem
}



data class CategoryItem(
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
)

@JvmInline
value class Name(
    val value: String
)

@JvmInline
value class Icon(
    val value: ImageVector
)

@JvmInline
value class CategoryColor(
    val value: Color
)