package com.example.cleverex.displayable.category

import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.Name


data class CategoryItemDisplayable (
    val name: Name,
    val icon: Icon,
    val color: CategoryColor
)