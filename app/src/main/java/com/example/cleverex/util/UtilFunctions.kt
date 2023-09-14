package com.example.cleverex.util

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.google.firebase.storage.FirebaseStorage
import io.realm.kotlin.types.RealmInstant
import timber.log.Timber
import java.time.Instant

/**
 * Download images from Firebase asynchronously.
 * This function returns imageUri after each successful download.
 * */
fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            if (remoteImagePath.trim().isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        Timber.d("DownloadURL: $it")
                        onImageDownload(it)
                        if (remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index) {
                            onReadyToDisplay()
                        }
                    }.addOnFailureListener {
                        onImageDownloadFailed(it)
                    }
            }
        }
    }
}

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