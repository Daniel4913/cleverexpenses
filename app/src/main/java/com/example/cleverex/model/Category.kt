package com.example.cleverex.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


class Category(
    val name: String = "",
    val icon: ImageVector,
    val color: Color,
    //todo date range - kiedy chce przefiltrowac kategorie z konkretnej
    )

@JvmInline
value class CategoryName(
    val value: String
)
@JvmInline
value class CategoryIcon(
    val value: ImageVector
)
@JvmInline
value class CategoryColor(
    val value: Color
)