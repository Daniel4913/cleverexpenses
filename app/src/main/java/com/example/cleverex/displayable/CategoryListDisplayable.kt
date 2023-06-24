package com.example.cleverex.displayable

data class CategoryListDisplayable (
    val elements: List<CategoryItemDisplayable>
): List<CategoryItemDisplayable> by elements