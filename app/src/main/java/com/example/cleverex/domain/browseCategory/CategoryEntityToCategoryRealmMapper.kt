package com.example.cleverex.domain.browseCategory


class CategoryEntityToCategoryRealmMapper :
    MainMapper<CategoryEntity, CategoryRealm> {
    override fun map(from: CategoryEntity): CategoryRealm {
        return if (from._id != null)
            CategoryRealm(
            ).apply {
                _id = from._id
                name = from.name.value
                icon = from.icon.value
                categoryColor = from.categoryColor.value
            }
        else CategoryRealm().apply {
            name = from.name.value
            icon = from.icon.value
            categoryColor = from.categoryColor.value
        }
    }
}