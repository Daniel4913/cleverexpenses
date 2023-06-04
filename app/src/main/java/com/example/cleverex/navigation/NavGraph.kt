package com.example.cleverex.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cleverex.presentation.components.DisplayAlertDialog
import com.example.cleverex.presentation.screens.auth.AuthenticationScreen
import com.example.cleverex.presentation.screens.auth.AuthenticationViewModel
import com.example.cleverex.presentation.screens.addBill.BillScreen
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.billOverview.BillOverviewScreen
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.home.HomeScreen
import com.example.cleverex.presentation.screens.home.HomeViewModel
import com.example.cleverex.util.Constants.APP_ID
import com.example.cleverex.util.Constants.ADD_BILL_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToAddBill = {
                navController.navigate(Screen.AddBill.route)
            },
            navigateToAddBillWithArgs = {
                navController.navigate(Screen.AddBill.passBillId(billId = it))
            },
            navigateToBillOverview = {
                navController.navigate(Screen.BillOverview.passBillId(billId = it))
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },

            onDataLoaded = onDataLoaded
        )
        addBillRoute(
            navigateBack = {
                navController.popBackStack()
            })
        billOverviewRoute(
            navigateBack = {
                navController.popBackStack()
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            // because we dont have data to observe like in homeRoute we're passing Unit: (key1 = Unit) - so this will trigger only once.
            onDataLoaded()
        }

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
//            onSuccessfulFirebaseSignIn = { tokenId ->
//                viewModel.signInWithMongoAtlas(
//                    tokenId = tokenId,
//                    onSuccess = {
//                        messageBarState.addSuccess("succesfully authenticated")
//                        viewModel.setLoading(false)
//                    },
//                    onError = {
//                        messageBarState.addError(it)
//                        viewModel.setLoading(false)
//                    }
//                )
//            },
//            onFailedFirebaseSignIn = {
//                messageBarState.addError(it)
//                viewModel.setLoading(false)
//            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun NavGraphBuilder.homeRoute(
    navigateToAddBill: () -> Unit,
    navigateToAddBillWithArgs: (String) -> Unit,
    navigateToBillOverview: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val bills by viewModel.fakeBills
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = bills) {
            if (bills !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            bills = bills,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = { signOutDialogOpened = true },
            navigateToAddBill = navigateToAddBill,
            navigateToAddBillWithArgs = navigateToAddBillWithArgs, // for editing?
            navigateToBillOverview = navigateToBillOverview
        )


        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out from your google account?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            }
        )
    }
}


fun NavGraphBuilder.addBillRoute(navigateBack: () -> Unit) {
    composable(
        route = Screen.AddBill.route,
        arguments = listOf(navArgument(name = ADD_BILL_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel: AddBillViewModel = viewModel()
        val uiState = viewModel.uiState
        val context = LocalContext.current

        BillScreen(
            uiState = uiState,
            onShopChanged = { viewModel.setShop(shop = it) },
            onAddressChanged = { viewModel.setAddress(address = it) },
            onPriceChanged = { viewModel.setPrice(price = it.toDouble()) },
            onBackPressed = navigateBack,
            onDeleteConfirmed = {
                viewModel.deleteBill(
                    onSuccess = {
                        Toast.makeText(context, "Bill successfully deleted", Toast.LENGTH_SHORT)
                            .show()
                        navigateBack()
                    },
                    onError = { message ->
                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onDateTimeUpdated = { viewModel.updateDateTime(it) },
            onSaveClicked = {
//                viewModel.upsertBill(
//                    onSuccess = navigateBack,
//                    onError = { message ->
//                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
//                    }
//                )
            }
        )
    }
}

fun NavGraphBuilder.billOverviewRoute(navigateBack: () -> Unit) {
    composable(
        route = Screen.BillOverview.route,
        arguments = listOf(navArgument(name = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        val viewModel: BillOverviewViewModel = viewModel()
        viewModel.uiState.billId?.let { it1 ->
            BillOverviewScreen(
                billId = it1
            )
        }
    }
}

