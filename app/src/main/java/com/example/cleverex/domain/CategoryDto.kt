package com.example.cleverex.domain

import com.example.cleverex.model.CategoryColor
import com.example.cleverex.model.Icon
import com.example.cleverex.model.Name
import java.util.UUID

data class CategoryDto (
 val name: Name,
 val icon: Icon,
 val categoryColor: CategoryColor
){
 val _id: String = UUID.randomUUID().toString()
}
