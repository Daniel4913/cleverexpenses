package com.example.cleverex.displayable.category

import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.Name
import org.mongodb.kbson.ObjectId

data class CategoryDisplayable(
    val id: ObjectId,
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
)