package com.example.cleverex.domain.browseCategory

import com.example.cleverex.displayable.category.CategoryDisplayable
import org.mongodb.kbson.ObjectId

class CategoryEntityToDisplayableMainMapper : MainMapper<CategoryEntity, CategoryDisplayable> {
    override fun map(from: CategoryEntity): CategoryDisplayable {
        return CategoryDisplayable(
//            id = ObjectId.invoke(from.id), // invalid hexadecimal representation of an ObjectId: [48abcaa9-8cc4-4be6-b026-1d08f5cfef9a]
            id = ObjectId.invoke(),
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }

}
