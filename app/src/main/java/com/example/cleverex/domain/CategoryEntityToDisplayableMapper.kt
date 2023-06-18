package com.example.cleverex.domain

import com.example.cleverex.displayable.CategoryDisplayable
import org.mongodb.kbson.ObjectId

class CategoryEntityToDisplayableMapper : Mapper<CategoryEntity, CategoryDisplayable> {
    override fun map(from: CategoryEntity): CategoryDisplayable {
        return CategoryDisplayable(
            id = ObjectId.invoke(from.id),
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }

}
