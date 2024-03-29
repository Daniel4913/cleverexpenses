package com.example.cleverex.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cleverex.util.Constants.CLIENT_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import timber.log.Timber
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
    navigateToHome: () -> Unit,
//    userEmail: String,
//    onUserEmailChanged: (String) -> Unit,
//    userPassword: String,
//    onUserPasswordChanged: (String) -> Unit,
    onEmailLoginClicked: (String, String) -> Unit,
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            Column() {
                LoginViaEmailPassword(
                    onEmailLoginClicked = { email, password ->
                        if (firebaseUser == null) {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        firebaseUser = firebaseAuth.currentUser
                                        onEmailLoginClicked(email, password)
                                    } else {
                                        // Jeśli błąd wynika z nieistniejącego użytkownika, próbujemy stworzyć konto
                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { createTask ->
                                                if (createTask.isSuccessful) {
                                                    firebaseUser = firebaseAuth.currentUser
                                                    onEmailLoginClicked(email, password)
                                                    Timber.d("// Konto zostało pomyślnie utworzone ${firebaseUser}")
                                                } else {
                                                    Timber.d("// Obsłuż błąd, np. jeśli konto z takim adresem e-mail już istnieje")
                                                }
                                            }
                                    }
                                }
                        }
                    }
                )
            }

//            ContentWithMessageBar(messageBarState = messageBarState) {
////                AuthenticationContent(
////                    loadingState = loadingState,
////                    onButtonClicked = onButtonClicked
////                )
//            }
        }
    )



    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->

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

fun login(email: String, password: String) {

}

@Composable
fun LoginViaEmailPassword(
    onEmailLoginClicked: (String, String) -> Unit
) {
    var userEmail by remember { mutableStateOf("test@test.test") }
    var userPassword by remember { mutableStateOf("testtest") }


    Column(modifier = Modifier.fillMaxSize()) {
        TextField(value = userEmail, onValueChange = { newValue -> userEmail = newValue })
        TextField(value = userPassword, onValueChange = { newValue -> userPassword = newValue })
        Button(onClick = {
            onEmailLoginClicked(userEmail, userPassword)
        }) {
            Text(text = "Login")
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
