package com.example.cleverex.domain.browseCategory


data class CategoryEntity(
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
)

interface Category {
    fun create(name: Name, icon: Icon, categoryColor: CategoryColor): CategoryItem
}

// odwrocenie zaleznosci !!!

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
    val value: String
)

@JvmInline
value class CategoryColor(
    val value: Long = 0xFF006972
)