package com.example.cleverex.domain.browseCategory

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class CategoryRealm : EmbeddedRealmObject {
    var name: String = ""
    var icon: String = ""
    var categoryColor: Long = 0
}