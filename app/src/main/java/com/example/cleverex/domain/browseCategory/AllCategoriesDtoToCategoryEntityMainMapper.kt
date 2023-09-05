package com.example.cleverex.domain.browseCategory


class AllCategoriesDtoToCategoryEntityMainMapper : MainMapper<CategoryDto, CategoryEntity> {
    override fun map(from: CategoryDto): CategoryEntity {
        return CategoryEntity(
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }
}


class CategoryEntityToCategoryRealmMapper : MainMapper<CategoryEntity, CategoryRealm> {
    override fun map(from: CategoryEntity): CategoryRealm {
        return CategoryRealm(
        ).apply {
            name = from.name.value
            icon = from.icon.value
//            categoryColor = from.categoryColor.value
        }
    }

}