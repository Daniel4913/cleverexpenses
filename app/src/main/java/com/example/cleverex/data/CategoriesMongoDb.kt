package com.example.cleverex.data

import com.example.cleverex.domain.browseCategory.CategoryColor
import com.example.cleverex.domain.browseCategory.CategoryEntity
import com.example.cleverex.domain.browseCategory.CategoryEmbedded
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.domain.browseCategory.Icon
import com.example.cleverex.domain.browseCategory.MainMapper
import com.example.cleverex.domain.browseCategory.Name
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.first
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.lang.Exception

class CategoriesMongoDb : BaseRealmRepository(), CategoriesRepository {

    override suspend fun getCategories(): List<CategoryRealm> {
        return if (user != null) {
            try {
                val results =
                    realm.query<CategoryRealm>("ownerId == '${user.id}'")
                        .sort("name", Sort.ASCENDING)
                        .asFlow()
                        .first()
                val list: RealmResults<CategoryRealm> = results.list
                list
            } catch (e: Exception) {
                Timber.d(t = e, "${e.message}")
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getCategory(categoryId: ObjectId) {
        if (user != null) {
            val categoryFlow =
                realm.query<CategoryRealm>("_id == $0", categoryId).asFlow()
            categoryFlow.collect { category ->
                val billItem = category.list.firstOrNull()
                if (billItem != null) {

                    Timber.d("Znaleziono category: $billItem")
                } else {

                }
            }
        }
    }


    override suspend fun insertCategory(category: CategoryRealm) {
        if (user != null) {
            realm.write {
                try {
                    copyToRealm(category.apply {
                        ownerId = user.id
                    })
                } catch (e: Exception) {
                    Timber.e("Wystąpił błąd podczas dodawania kategorii: ${e.message}")
                }
            }
        } else {
            Timber.w("Użytkownik nie jest zalogowany. Nie można dodać kategorii.")
        }
    }

    override suspend fun updateCategory(category: CategoryRealm) {
        if (user != null) {
            realm.write {
                try {
                    val queriedCategory =
                        query<CategoryRealm>(query = "_id == $0", category._id).first().find()
                    if (queriedCategory != null) {
                        queriedCategory.name = category.name
                        queriedCategory.icon = category.icon
                        queriedCategory.categoryColor = category.categoryColor
                    } else {
                        Timber.d("queried to update category does not exist ${category._id}")
                    }

                } catch (e: Exception) {
                    Timber.d("Some problem occurred while updating category: $e ${e.message}")
                }
            }
        }
    }

    override suspend fun deleteCategory(id: ObjectId) {
        return if (user != null) {
            realm.write {
                val category = query<CategoryRealm>(
                    query = "_id == $0 AND ownerId == $1", id, user.id
                ).first().find()
                if (category != null) {
                    try {
                        delete(category)
                    } catch (e: Exception) {
                        Timber.d("Error $e ${e.message}")
                    }
                } else {
                    Timber.d("category=null Category does not exist?")
                }
            }
        } else {
            Timber.d("user=null Category does not exist?")
        }
    }
}


class CategoryEntityToRealmMapper : MainMapper<CategoryEntity, CategoryEmbedded> {
    override fun map(from: CategoryEntity): CategoryEmbedded {
        return CategoryEmbedded()
            .apply {
                this.name = from.name.value
                this.icon = from.icon.value
                this.categoryColor = from.categoryColor.value.toString()
            }
    }
}
