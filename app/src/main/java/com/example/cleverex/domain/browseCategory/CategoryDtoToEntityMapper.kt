package com.example.cleverex.domain.browseCategory


class CategoryDtoToEntityMapper: Mapper<CategoryDto, CategoryEntity> {
    override fun map(from: CategoryDto): CategoryEntity {
        return CategoryEntity(
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }
}

interface Mapper<in From, out To> {
    fun map(from: From): To
}
