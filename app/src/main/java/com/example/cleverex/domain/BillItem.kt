package com.example.cleverex.domain

import com.example.cleverex.domain.browseCategory.CategoryRealm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class BillItem : EmbeddedRealmObject {
//    @PrimaryKey
//    var _id: ObjectId = ObjectId.invoke()
//    var billId: ObjectId = ObjectId()
    var name: String = ""
    var price: Double = 0.0
    var quantity: Double = 0.0
    var unit: String = ""
    var totalPrice: Double = 0.0
    var categories: RealmList<CategoryRealm> = realmListOf()
}