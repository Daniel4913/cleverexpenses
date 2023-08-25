package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.AllCategoriesDto
import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.CategoryEntity
import com.example.cleverex.domain.browseCategory.CategoryItem
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.MainMapper
import com.example.cleverex.domain.browseCategory.Name
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.lang.Exception

class CategoriesMongoDb : BaseRealmRepository(), CategoriesRepository {

    override fun getCategories(): List<CategoryEntity> {
        return if (user != null) {
            try {
                val results =
                    realm.query<CategoryRealm>("ownerId == '${user.id}'")
                        .sort("name", Sort.ASCENDING)
                        .find()

                val mapper = CategoryRealmToEntityMapper()
                results.map { mapper.map(it) }
            } catch (e: Exception) {
                Timber.d(t = e, "${e.message}")
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    override fun getCategory(): CategoryEntity {
        TODO("Not yet implemented")
    }

    override fun insertCategory(): CategoryEntity {
        TODO("Not yet implemented")
    }

    override fun updateCategory(): CategoryEntity {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(): CategoryEntity {
        TODO("Not yet implemented")
    }
}

class CategoryRealmToEntityMapper : MainMapper<CategoryRealm, CategoryEntity> {
    override fun map(from: CategoryRealm): CategoryEntity {
        return CategoryEntity(
            name = Name(value = from.name),
            icon = Icon(value = from.icon),
            categoryColor = CategoryColor(
                value = from.categoryColor.toLong()
            )
        )
    }
}

class CategoryEntityToRealmMapper : MainMapper<CategoryEntity, CategoryRealm> {
    override fun map(from: CategoryEntity): CategoryRealm {
        return CategoryRealm()
            .apply {
                this.name = from.name.value
                this.icon = from.icon.value
                this.categoryColor = from.categoryColor.value.toString()
            }
    }
}
