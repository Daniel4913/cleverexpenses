package com.example.cleverex.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class BillItem : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var name: String = ""
    var price: Double = 0.0
    var quantity: Double = 0.0
    var unit: String = ""
    var totalPrice: Double = 0.0
    var category: String = "Sweets"
}

// categories: List<Category> = realmListOf()

//class Item(
//    val name: ProductName, // kartofle
//    val price: ProductPrice, // 3,36
//    val quanity: Double, // 2,26
//    val unit: Unit, //  MyUnits Kg (sztuki, litry i co tam jeszcze. chociaz litry chyba nie xD)
//    val totalPrice: Double, // 7,59
//    val waluta: Current, // zloty
//)