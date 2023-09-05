package com.example.cleverex.domain

import com.example.cleverex.domain.browseCategory.CategoryRealm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class BillItem : EmbeddedRealmObject {
    var name: String = ""
    var quantity: Double = 0.0
    var price: Double = 0.0
    //unit_price
    var totalPrice: Double = 0.0
    var unit: String = ""
    var categories: RealmList<CategoryRealm> = realmListOf()
}