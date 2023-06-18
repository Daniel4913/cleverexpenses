package com.example.cleverex.presentation.displayable

data class CategoryListDisplayable (
    val elements: List<CategoryItemDisplayable>
): List<CategoryItemDisplayable> by elements