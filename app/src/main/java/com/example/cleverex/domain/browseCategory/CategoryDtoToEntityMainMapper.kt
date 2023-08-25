package com.example.cleverex.domain.browseCategory

import com.example.cleverex.domain.Bill
import com.example.cleverex.util.RequestState
import kotlinx.coroutines.flow.Flow


class CategoryDtoToEntityMainMapper: MainMapper<CategoryDto, CategoryEntity> {
    override fun map(from: CategoryDto): CategoryEntity {
        return CategoryEntity(
            name = from.name,
            icon = from.icon,
            categoryColor = from.categoryColor
        )
    }
}

interface MainMapper<in From, out To> {
    fun map(from: From): To
}
interface FlowMapper<in From, out To> {
    suspend fun map(from: Flow<RequestState<List<Bill>>>): To
}


