package com.example.cleverex.domain.browseCategory


class AllCategoriesDtoToCategoryEntityMapper : Mapper<CategoryDto, CategoryEntity> {
    override fun map(from: CategoryDto): CategoryEntity {
        return CategoryEntity(
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }
}
