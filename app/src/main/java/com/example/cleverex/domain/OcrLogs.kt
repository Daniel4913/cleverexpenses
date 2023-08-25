package com.example.cleverex.domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

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