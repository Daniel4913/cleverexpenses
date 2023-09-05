package com.example.cleverex.domain.browseCategory


class CategoryEntityToCategoryRealmMapper :
    MainMapper<CategoryEntity, CategoryRealm> {
    override fun map(from: CategoryEntity): CategoryRealm {
        return CategoryRealm(
        ).apply {
            name = from.name.value
            icon = from.icon.value
            categoryColor = from.categoryColor.value
        }
    }
}