package com.example.cleverex.domain.browseCategory

import com.example.cleverex.displayable.category.CategoryDisplayable
import org.mongodb.kbson.ObjectId

class CategoryEntityToDisplayableMainMapper : MainMapper<CategoryEntity, CategoryDisplayable> {
    override fun map(from: CategoryEntity): CategoryDisplayable {
        return CategoryDisplayable(
            id = ObjectId.invoke(),
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }

}

class ListCategoryEntityToListDisplayableMapper :
    MainMapper<List<CategoryEntity>, List<CategoryDisplayable>> {
    override fun map(from: List<CategoryEntity>): List<CategoryDisplayable> {
        return from.map { categoryEntity ->
            CategoryDisplayable(
                id = categoryEntity._id,
                name = categoryEntity.name,
                icon = categoryEntity.icon,
                categoryColor = categoryEntity.categoryColor,
            )
        }
    }
}