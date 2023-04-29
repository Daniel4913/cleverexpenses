package com.example.cleverex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.cleverex.data.MongoDB
import com.example.cleverex.navigation.Screen
import com.example.cleverex.navigation.SetupNavGraph

import com.example.cleverex.ui.theme.AppTheme
import com.example.cleverex.util.Constants.APP_ID
import com.google.firebase.FirebaseApp
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
            .setKeepOnScreenCondition {
            keepSplashOpened
        }
//        FirebaseApp.initializeApp(this)
        WindowCompat.setDecorFitsSystemWindows(window, false) // to make transparent appbar

        setContent {
            AppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    }
                )
            }
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if(user != null && user.loggedIn)Screen.Home.route
    else Screen.Authentication.route
}