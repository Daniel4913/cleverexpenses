package com.example.cleverex.domain.browseCategory

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

open class CategoryEmbedded : EmbeddedRealmObject {
    var id: ObjectId = ObjectId()
    var name: String = ""
    var icon: String = ""
    var categoryColor: String = ""
}

