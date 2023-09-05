package com.example.cleverex.domain.browseCategory

import org.mongodb.kbson.ObjectId


data class CategoryEntity(
    val _id: ObjectId = ObjectId.invoke(),
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
    val value: String
)