package com.example.cleverex.displayable.category

data class CategoryListDisplayable (
    val elements: List<CategoryItemDisplayable>
): List<CategoryItemDisplayable> by elements