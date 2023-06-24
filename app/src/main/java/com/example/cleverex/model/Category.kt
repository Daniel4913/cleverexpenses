package com.example.cleverex.model

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
    val value: Long = 0xFF006972 // val md_theme_light_primary = Color(0xFF006972)| A wiec Long a nie string hex.
    // TODO:  // Have to contain # and length > 5 - to juz nie potzebne
)