package com.example.cleverex.domain.browseCategory



class CreateCategoryUseCase(
) : CreateCategory {
    override fun create(
        name: Name,
        icon: Icon,
        categoryColor: CategoryColor
    ): Category {
        return Category(
            name = name,
            icon = icon,
            categoryColor = categoryColor
        )
    }
}