package com.example.cleverex.displayable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ProductCategoryDisplayable(
    val name: String,
    val icon: String,
    val color: Long,
    val quantity: Int,
    val unit: String,
    val price: Double,
    val currency: String = "zł"
)