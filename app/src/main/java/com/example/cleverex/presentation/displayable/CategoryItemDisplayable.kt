package com.example.cleverex.presentation.displayable

import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name

data class CategoryItemDisplayable (
    val name: Name,
    val icon: Icon,
    val color: CategoryColor
)
