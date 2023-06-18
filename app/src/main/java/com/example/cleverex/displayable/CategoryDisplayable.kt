package com.example.cleverex.displayable

import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name
import org.mongodb.kbson.ObjectId

data class CategoryDisplayable(
    val id: ObjectId,
    val name: Name,
    val icon: Icon,
    val categoryColor: CategoryColor
)