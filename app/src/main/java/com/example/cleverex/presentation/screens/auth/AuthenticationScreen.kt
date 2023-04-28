package com.example.cleverex.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.cleverex.util.Constants.CLIENT_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception


@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
//    onSuccessfulFirebaseSignIn: (String) -> Unit,
//    onFailedFirebaseSignIn: (Exception) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingState = loadingState,
                    onButtonClicked = onButtonClicked
                )
            }
        }
    )
    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth tokenId:", tokenId)
            onTokenIdReceived(tokenId)
            messageBarState.addSuccess("Successfully authenticated")
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
//            FirebaseAuth.getInstance().signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if(task.isSuccessful) {
//                        onSuccessfulFirebaseSignIn(tokenId)
//                    } else {
//                        task.exception?.let { it -> onFailedFirebaseSignIn(it) }
//                    }
//                }
        },
        onDialogDismissed = { message ->
            Log.d("Auth tokenId:", message)
            onDialogDismissed(message)
        }
    )

    LaunchedEffect(key1 = authenticated) { // launched effect will be triggered when parameter 'authenticated' changes
        if (authenticated) {
            navigateToHome()
        }
    }
}


//@ExperimentalMaterial3Api
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun AuthenticationScreen(
//    authenticated: Boolean,
//    loadingState: Boolean,
//    oneTapState: OneTapSignInState,
//    messageBarState: MessageBarState,
//    onButtonClicked: () -> Unit,
//    onSuccessfulFirebaseSignIn: (String) -> Unit,
//    onFailedFirebaseSignIn: (Exception) -> Unit,
//    onDialogDismissed: (String) -> Unit,
//    navigateToHome: () -> Unit
//) {
//    Scaffold(
//        modifier = Modifier
//            .background(MaterialTheme.colorScheme.surface)
//            .statusBarsPadding()
//            .navigationBarsPadding(),
//        content = {
//
//            ContentWithMessageBar(messageBarState = messageBarState) {
//                AuthenticationContent(
//                    loadingState = loadingState,
//                    onButtonClicked = onButtonClicked
//                )
//            }
//        }
//    )
//
//    OneTapSignInWithGoogle(
//        state = oneTapState,
//        clientId = CLIENT_ID,
//        onTokenIdReceived = { tokenId ->
//            val credentials = GoogleAuthProvider.getCredential(tokenId, null)
//            FirebaseAuth.getInstance().signInWithCredential(credentials)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        onSuccessfulFirebaseSignIn(tokenId)
//
//                    } else {
//                        task.exception?.let { it -> onFailedFirebaseSignIn(it) }
//                    }
//                }
//        },
//        onDialogDismissed = { message ->
//            onDialogDismissed(message)
//        }
//    )
//
//    LaunchedEffect(key1 = authenticated) {
//        // this LaunchedEffect will be triggered only if this authenticated value changes
//        if (authenticated) {
//            navigateToHome()
//        }
//    }
//}


//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import com.example.cleverex.util.Constants.CLIENT_ID
//import com.stevdzasan.messagebar.ContentWithMessageBar
//import com.stevdzasan.messagebar.MessageBarState
//import com.stevdzasan.onetap.OneTapSignInState
//import com.stevdzasan.onetap.OneTapSignInWithGoogle
//import java.lang.Exception
//
//@ExperimentalMaterial3Api
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun AuthenticationScreen(
//    authenticated: Boolean,
//    loadingState: Boolean,
//    oneTapState: OneTapSignInState,
//    messageBarState: MessageBarState,
//    onButtonClicked: () -> Unit,
//    onTokenIdReceived: (String) -> Unit,
//    onDialogDismissed: (String) -> Unit,
//    navigateToHome: () -> Unit
//) {
//    Scaffold(
//        modifier = Modifier
//            .background(MaterialTheme.colorScheme.surface)
//            .statusBarsPadding()
//            .navigationBarsPadding(),
//        content = {
//
//            ContentWithMessageBar(messageBarState = messageBarState) {
//                AuthenticationContent(
//                    loadingState = loadingState,
//                    onButtonClicked = onButtonClicked
//                )
//            }
//        }
//    )
//
//    OneTapSignInWithGoogle(
//        state = oneTapState,
//        clientId = CLIENT_ID,
//        onTokenIdReceived = { tokenId ->
//            onTokenIdReceived(tokenId)
//        },
//        onDialogDismissed = { message ->
//            onDialogDismissed(message)
//        }
//    )
//
//    LaunchedEffect(key1 = authenticated) {
//        // this LaunchedEffect will be triggered only if this authenticated value changes
//        if (authenticated) {
//            navigateToHome()
//        }
//    }
//}
