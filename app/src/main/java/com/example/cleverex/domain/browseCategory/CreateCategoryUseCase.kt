package com.example.cleverex.domain.browseCategory


class CreateCategoryUseCase(
) : Category {
    override fun create(
        name: Name,
        icon: Icon,
        categoryColor: CategoryColor
    ): CategoryItem {
        return CategoryItem(
            name = name,
            icon = icon,
            categoryColor = categoryColor
        )
    }
}