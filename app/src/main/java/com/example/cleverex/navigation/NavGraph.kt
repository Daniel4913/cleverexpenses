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
import com.example.cleverex.data.datastore.BudgetDataStore
import com.example.cleverex.presentation.components.DisplayAlertDialog
import com.example.cleverex.presentation.screens.auth.AuthenticationScreen
import com.example.cleverex.presentation.screens.auth.AuthenticationViewModel
import com.example.cleverex.presentation.screens.addBill.AddBillScreen
import com.example.cleverex.presentation.screens.addBill.AddBillViewModel
import com.example.cleverex.presentation.screens.addItems.AddItemsScreen
import com.example.cleverex.presentation.screens.billOverview.BillOverviewScreen
import com.example.cleverex.presentation.screens.billOverview.BillOverviewViewModel
import com.example.cleverex.presentation.screens.budget.BudgetScreen
import com.example.cleverex.presentation.screens.budget.BudgetViewModel
import com.example.cleverex.presentation.screens.categories.BrowseCategoriesViewModel
import com.example.cleverex.presentation.screens.categories.BrowseCategoriesScreen
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
import org.koin.androidx.compose.koinViewModel

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
//            onDataLoaded = onDataLoaded
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
            navigateToBrowseCategories = {
                navController.navigate(Screen.BrowseCategories.route)
            },
            onDataLoaded = onDataLoaded,
            navigateToSetBudget = {
                navController.navigate(Screen.Budget.route)
            }
        )
        addBillRoute(
            navigateBack = {
                navController.popBackStack()
            },
            onAddItemsClicked = {
                navController.navigate(Screen.AddItems.route)

            })
        addItemsRoute(
            navigateBack = {
                navController.popBackStack()
            })
        billOverviewRoute(
            navigateBack = {
                navController.popBackStack()
            },
            onEditPressed = {},
        )
        budgetRoute(
            navigateBack = {
                navController.popBackStack()
            },
        )
        browseCategories(
            navigateBack = {
                navController.popBackStack()
            },
            onCategoryClicked = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
//    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            // because we dont have data to observe like in homeRoute we're passing Unit: (key1 = Unit) - so this will trigger only once.
//            onDataLoaded()
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
//                viewModel.signInWithMongoAtlas(
//                    tokenId = tokenId,
//                    onSuccess = {
//                        messageBarState.addSuccess("Successfully authenticated")
//                        viewModel.setLoading(false)
//                    },
//                    onError = {
//                        messageBarState.addError(it)
//                        viewModel.setLoading(false)
//                    }
//                )
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome,
            onEmailLoginClicked = { userEmail, userPassword ->
                viewModel.signInWithMongoAtlas(
                    userEmail = userEmail,
                    userPassword = userPassword,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    })
            },

            )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun NavGraphBuilder.homeRoute(
    navigateToAddBill: () -> Unit,
    navigateToAddBillWithArgs: (String) -> Unit,
    navigateToBillOverview: (String) -> Unit,
    navigateToAuth: () -> Unit,
    navigateToBrowseCategories: () -> Unit,
    navigateToSetBudget: () -> Unit,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = koinViewModel()
        val bills by viewModel.bills
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val weekBudget = BudgetDataStore

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
            navigateToBillOverview = navigateToBillOverview,
            navigateToBrowseCategories = navigateToBrowseCategories,
            navigateToSetBudget = navigateToSetBudget,
            weekBudget = viewModel.weekBudget.budget
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


fun NavGraphBuilder.addBillRoute(
    navigateBack: () -> Unit,
    onAddItemsClicked: () -> Unit
) {
    composable(
        route = Screen.AddBill.route,
        arguments = listOf(navArgument(name = ADD_BILL_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel: AddBillViewModel = koinViewModel()
        val uiState = viewModel.uiState
        val context = LocalContext.current
        val imageState = viewModel.imageState
        val categories: BrowseCategoriesViewModel = koinViewModel()


        AddBillScreen(
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
                viewModel.upsertBill(
                    onSuccess = navigateBack,
                    onError = { message ->
                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onAddItemClicked = { viewModel.createAndAddBillItem() },
            chosenImageData = imageState.image.firstOrNull(),
            onImageSelect = { viewModel.addImage(it) },
            onNameChanged = { viewModel.setName(name = it) },
            onQuantityChanged = { viewModel.setQuantity(quantity = it) },
            onProductPriceChanged = { viewModel.setProductPrice(productPrice = it) },
            onQuantityTimesPriceChanged = { viewModel.setQuantityTimesPrice(quantityTimesPrice = it) },
            onUnparsedValuesChanged = { viewModel.setUnparsedValues(unparsedValues = it) },
            categories = categories.uiState.collectAsState()
        )
    }
}

fun NavGraphBuilder.addItemsRoute(
    navigateBack: () -> Unit
) {
    composable(
        route = Screen.AddItems.route,
//        arguments = listOf(navArgument(name = ADD_BILL_ITEMS_SCREEN_ARGUMENT_KEY)
//        {
//            type = NavType.StringType
//            nullable = true
//            defaultValue = null
//        })
    ) {
        val viewModel: AddBillViewModel = koinViewModel()
        AddItemsScreen(
            uiState = viewModel.uiState
        )
    }
}

fun NavGraphBuilder.billOverviewRoute(
    navigateBack: () -> Unit,
    onEditPressed: () -> Unit,
) {
    composable(
        route = Screen.BillOverview.route,
        arguments = listOf(navArgument(name = BILL_OVERVIEW_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) {
        val viewModel: BillOverviewViewModel = koinViewModel()
        BillOverviewScreen(
            uiState = viewModel.uiState,
            onBackPressed = navigateBack,
            onEditPressed = onEditPressed,
            onDeleteConfirmed = onEditPressed,
            onDateTimeUpdated = { viewModel.updateDateTime(it) },
        )
    }
}

fun NavGraphBuilder.budgetRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = Screen.Budget.route
    ) {
        val viewModel: BudgetViewModel = koinViewModel()
        BudgetScreen(
            onBackPressed = navigateBack,
            onBudgetChange = {
                viewModel.setBudget(budget = it.toDouble())
            },
            weeklyBudget = viewModel.uiState.budget
        )
    }
}

fun NavGraphBuilder.browseCategories(
    navigateBack: () -> Unit,
    onCategoryClicked: () -> Unit
) {
    composable(
        route = Screen.BrowseCategories.route
    ) {
        val viewModel: BrowseCategoriesViewModel = koinViewModel()
        val uiState = viewModel.uiState

        BrowseCategoriesScreen(
            uiState = uiState,
            onBackPressed = navigateBack,
            onCategoryPressed = { /*TODO*/ },
            showColorPicker = {
                viewModel.showColorPicker(it)
            },
            onNameChanged = { viewModel.setName(it) },
            onIconChanged = { viewModel.setIcon(it) },
            onColorChanged = { viewModel.setColor(it) },
            onCreateClicked = { viewModel.insertCategory() }
        )
    }
}


