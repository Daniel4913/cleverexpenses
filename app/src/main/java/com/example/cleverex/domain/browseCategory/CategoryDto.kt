package com.example.cleverex.domain.browseCategory

import java.util.UUID

data class CategoryDto(
 val name: Name,
 val icon: Icon,
 val categoryColor: CategoryColor
){
 val _id: String = UUID.randomUUID().toString()
}