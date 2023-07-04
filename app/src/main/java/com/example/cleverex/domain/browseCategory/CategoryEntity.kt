package com.example.cleverex.domain.browseCategory

import java.util.UUID

data class CategoryEntity(
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
) {
    val id: String = UUID.randomUUID().toString()
}

interface CreateCategory {
    fun create(name: Name, icon: Icon, categoryColor: CategoryColor): Category
}

// odwrocenie zaleznosci !!!

data class Category(
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