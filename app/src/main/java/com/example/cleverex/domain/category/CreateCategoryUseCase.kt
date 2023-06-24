package com.example.cleverex.domain.category

import com.example.cleverex.model.Category
import com.example.cleverex.model.CategoryItem
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name

class CreateCategoryUseCase(
) : Category {
    override fun create(
        name: Name,
        icon: Icon,
        categoryColor: com.example.cleverex.model.CategoryColor
    ): CategoryItem {
        return CategoryItem(
            name = name,
            icon = icon,
            categoryColor = categoryColor
        )
    }
}