package com.example.cleverex.domain.browseCategory

class ListCategoryRealmToListEntityMapper :
    MainMapper<List<CategoryRealm>, List<CategoryEntity>> {
    override fun map(from: List<CategoryRealm>): List<CategoryEntity> {
        return from.map { categoryRealm ->
            CategoryEntity(
                _id = categoryRealm._id,
                name = Name(value = categoryRealm.name),
                icon = Icon(value = categoryRealm.icon),
                categoryColor = CategoryColor(
                    value = categoryRealm.categoryColor
                )
            )
        }
    }
}