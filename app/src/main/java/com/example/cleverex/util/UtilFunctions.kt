package com.example.cleverex.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}

// chcialem zrobic extensioin fun Color.calculateContentColor ale Color to companion object
fun calculateContentColor(containerColor: Color): Color {
    val containerBrightness = containerColor.luminance()
    val darkColor = Color.Black
    val lightColor = Color.White

    return if (containerBrightness > 0.5) darkColor else lightColor
}