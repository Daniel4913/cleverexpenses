package com.example.cleverex.domain

import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name
import java.util.UUID

data class CategoryEntity(
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
) {
    val id: String = UUID.randomUUID().toString()
}