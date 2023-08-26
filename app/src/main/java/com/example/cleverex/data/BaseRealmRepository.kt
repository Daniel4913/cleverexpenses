package com.example.cleverex.data

import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.BillItem
import com.example.cleverex.domain.OcrLogs
import com.example.cleverex.domain.browseCategory.CategoryRealm
import com.example.cleverex.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import timber.log.Timber

abstract class BaseRealmRepository {

    protected val app = App.create(APP_ID)
    protected val user = app.currentUser
    protected lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    protected fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user,
                setOf(
                    Bill::class,
                    BillItem::class,
                    OcrLogs::class,
                    CategoryRealm::class
                )
            ).initialSubscriptions { sub ->
                add(
                    query = sub.query<Bill>("ownerId == $0", user.id),
                    name = "User's Bill's"
                )
            }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)
        }
    }
}
