package com.example.cleverex.data

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
import org.mongodb.kbson.BsonObjectId
import timber.log.Timber
import java.lang.Exception

class CategoriesMongoDb : BaseRealmRepository(), CategoriesRepository {

//    override suspend fun getCategories(): List<CategoryEntity> {
//        return if (user != null) {
//            try {
//                val results =
//                    realm.query<CategoryRealm>("ownerId == '${user.id}'")
//                        .sort("name", Sort.ASCENDING)
//                        .find()
//
//                val mapper = CategoryRealmToEntityMapper()
//                results.map { mapper.map(it) }
//            } catch (e: Exception) {
//                Timber.d(t = e, "${e.message}")
//                emptyList()
//            }
//        } else {
//            emptyList()
//        }
//    }
//
//    //    override fun getCategory(): CategoryEntity {
////
////    }
//    override suspend fun getCategory() {
//        Timber.d("getCategory invoked")
//        val ajdi = BsonObjectId("64e8bf82c9dad7a4fe6e98af")
//        val categoryFlow = realm.query<CategoryRealm>("_id == $0", ajdi).asFlow()
//        categoryFlow.collect { category ->
//            val billItem = category.list.firstOrNull()
//            if (billItem != null) {
//                // Tutaj możesz zrobić coś z billItem
//                Timber.d("Znaleziono category: $billItem")
//            } else {
//                Timber.d("Nie znaleziono categ o ID: $ajdi")
//            }
//        }
//    }
//
//
//    override suspend fun insertCategory(category: CategoryRealm) {
//        if (user != null) {
//            realm.write {
//                try {
//                    val addedCategory = copyToRealm(category.apply {
//                        this.ownerId = user.id
//                    })
//                    addedCategory
//                } catch (e: Exception) {
//                    Timber.e("Wystąpił błąd podczas dodawania kategorii: ${e.message}")
//                }
//            }
//        } else {
//            Timber.w("Użytkownik nie jest zalogowany. Nie można dodać kategorii.")
//        }
//    }
//
//    override suspend fun updateCategory(): CategoryRealm {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteCategory(): CategoryRealm {
//        TODO("Not yet implemented")
//    }
//}
//
//class CategoryRealmToEntityMapper : MainMapper<CategoryRealm, CategoryEntity> {
//    override fun map(from: CategoryRealm): CategoryEntity {
//        return CategoryEntity(
//            name = Name(value = from.name),
//            icon = Icon(value = from.icon),
//            categoryColor = CategoryColor(
//                value = from.categoryColor.toLong()
//            )
//        )
//    }
//}
//
//class CategoryEntityToRealmMapper : MainMapper<CategoryEntity, CategoryRealm> {
//    override fun map(from: CategoryEntity): CategoryRealm {
//        return CategoryRealm()
//            .apply {
//                this.name = from.name.value
//                this.icon = from.icon.value
//                this.categoryColor = from.categoryColor.value.toString()
//            }
//    }
}
