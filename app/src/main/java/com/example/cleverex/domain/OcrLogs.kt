package com.example.cleverex.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

open class OcrLogs : EmbeddedRealmObject {
    var textBlocks: RealmList<MyTextBlock> = realmListOf()
}

open class MyTextBlock : EmbeddedRealmObject {
    var text: String = ""
    var boundingBox: String = ""
    var lines: RealmList<MyLine> = realmListOf()
}

open class MyLine : EmbeddedRealmObject {
    var text: String = ""
    var boundingBox: String = ""
    var elements: RealmList<MyElement> = realmListOf()
}

open class MyElement : EmbeddedRealmObject {
    var text: String = ""
    var boundingBox: String = ""
}
