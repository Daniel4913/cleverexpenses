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

    //    override fun getCategory(): CategoryEntity {
//
//    }
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

    override suspend fun updateCategory(): CategoryRealm {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(): CategoryRealm {
        TODO("Not yet implemented")
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
