package com.example.cleverex.presentation.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cleverex.R
import com.example.cleverex.presentation.GoogleButton


@Composable
fun AuthenticationContent(
    loadingState: Boolean,
    onButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
                .padding(all = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(weight = 10f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Logo"
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.auth_title),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    text = stringResource(id = R.string.auth_subtitle),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
            Column(
                modifier = Modifier.weight(weight = 2f),
                verticalArrangement = Arrangement.Bottom
            ) {
                GoogleButton(
                    loadingState = loadingState,
                    onClick = onButtonClicked
                )
            }
        }
    }
}



//@Composable
//fun AuthenticationContent(
//    loadingState: Boolean,
//    onButtonClicked: () -> Unit
//) {
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Column(
//            modifier = Modifier
//                .weight(9f)
//                .fillMaxWidth()
//                .padding(all = 40.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                modifier = Modifier.weight(weight = 10f),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//
//            ) {
//                Image(
//                    modifier = Modifier.size(120.dp),
//                    painter = painterResource(id = R.drawable.google_logo),
//                    contentDescription = "Google logo"
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Text(
//                    text = "Welcome back",
//                    fontSize = MaterialTheme.typography.titleLarge.fontSize
//                )
//                Text(
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
//                    text = "Please sign in to continue.",
//                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
//                )
//            }
//            Column(
//                modifier = Modifier.weight(weight = 2f), verticalArrangement = Arrangement.Bottom
//
//            ) {
//                GoogleButton(
//                    loadingState = loadingState,
//                    onClick = onButtonClicked
//                )
//
//            }
//        }
//    }
//}




//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import com.example.cleverex.R
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.example.cleverex.navigation.presentation.GoogleButton
//
//@Composable
//fun AuthenticationContent(
//    loadingState: Boolean,
//    onButtonClicked: () -> Unit,
//) {
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Column(
//            modifier = Modifier
//                .weight(9f)
//                .fillMaxWidth()
//                .padding(all = 40.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(9f)
//                    .fillMaxWidth()
//                    .padding(all = 10.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Image(
//                    modifier = Modifier.size(120.dp),
//                    painter = painterResource(id = R.drawable.google_logo),
//                    contentDescription = "Google logo."
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Text(
//                    text = "Welcome Back",
//                    fontSize = MaterialTheme.typography.titleLarge.fontSize
//                )
//                Text(
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
//                    text = "You need to Sign in to continue",
//                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
//                )
//            }
//            Column(
//                modifier = Modifier.weight(weight = 2f),
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                GoogleButton(
//                    loadingState = loadingState,
//                    onClick = onButtonClicked
//                )
//            }
//        }
//    }
//}