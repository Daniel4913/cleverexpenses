package com.example.cleverex.domain.browseCategory

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class CategoryRealm : EmbeddedRealmObject {
    //    @PrimaryKey
//    var _id: ObjectId = ObjectId.invoke()
//    var ownerId: String = ""
    var name: String = ""
    var icon: String = ""
    var categoryColor: String = ""
}