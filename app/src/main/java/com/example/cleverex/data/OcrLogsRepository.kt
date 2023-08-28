package com.example.cleverex.data

import com.example.cleverex.domain.OcrLogs
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import timber.log.Timber

interface OcrLogsRepositoryInterface {
    fun saveLog(ocrLogs: OcrLogs)
    fun checkLastSavedLogs()
}

object OcrLogsRepositoryImpl : OcrLogsRepositoryInterface {
    var finalOcrLogsList: RealmList<OcrLogs> = realmListOf()


    override fun saveLog(ocrLogs: OcrLogs) {
        finalOcrLogsList.add(ocrLogs)
    }

    override fun checkLastSavedLogs() {
        Timber.d("$finalOcrLogsList")
        finalOcrLogsList.forEach {
            Timber.d("lastSavedLogs: ${it.textBlocks}")
        }
    }
}
