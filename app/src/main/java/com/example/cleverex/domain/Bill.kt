package com.example.cleverex.domain

import com.example.cleverex.util.toRealmInstant
import com.google.mlkit.vision.text.Text.TextBlock
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
    var billTranscription: RealmList<OcrLogs>? = realmListOf()
}

open class OcrLogs : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var rawText: String = ""

    // TextBlock logs
    var textBlockText: String = ""
    var textBlockBoundingBox: String = ""

    // Line logs
    var lineText: String = ""
    var lineBoundingBox: String = ""
    var lineConfidence: String =""

    // Element logs
    var elementText: String = ""
    var elementBoundingBox: String = ""
    var elementLanguage: String = ""
    var elementConfidence: String =""
}

//    var shopAdress: String

// {"_id":{"$oid":"64302447235224eb7095938f"},"ownerId":"6429ec6ab5591ec35eb2a0ef","shop":"Lidl","billDate":{"$date":{"$numberLong":"1688479200000"}},"price":{"$numberDouble":"25677.22"}}
