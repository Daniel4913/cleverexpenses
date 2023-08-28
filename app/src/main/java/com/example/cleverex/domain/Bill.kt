package com.example.cleverex.domain

import com.example.cleverex.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.Instant

open class Bill : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var ownerId: String = ""
    var shop: String = ""
    var address: String? = ""
    var billDate: RealmInstant = Instant.now().toRealmInstant()
    var price: Double = 0.0
    var billItems: RealmList<BillItem> = realmListOf()
    var billImage: String? = ""
    var paymentMethod: String? = ""
    var ocrText: String? = ""
    var ocrPositions: RealmList<OcrLogs> = realmListOf()
}