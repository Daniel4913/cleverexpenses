package com.example.cleverex.presentation.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.cleverex.R
import com.example.cleverex.presentation.displayable.BillsByWeeks
import com.example.cleverex.util.RequestState

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    bills: RequestState<BillsByWeeks> ,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    navigateToAddBill: () -> Unit,
    navigateToAddBillWithArgs: (String) -> Unit,
    navigateToBillOverview: (String)->Unit

    ) {
    var padding by remember {
        // 'by' keyword - use actual value without a state
        mutableStateOf(PaddingValues())
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = onSignOutClicked
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(
                    scrollBehavior = scrollBehavior,
                    onMenuClicked = onMenuClicked
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                    onClick = navigateToAddBill,
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Add icon")
                }
            },
            content = {
                padding = it
                when (bills) {
                    is RequestState.Success<BillsByWeeks> -> {
                        HomeContent(
                            paddingValues = it,
                            datedBills = bills.data,
                            weekBudget = 100.00,
                            onBillClicked = navigateToBillOverview,
                            onWeekIndicatorClicked = {}
                        )

                    }
                    is RequestState.Error -> {
                        EmptyPage(title = "Error", subtitle = "${bills.error.message}")
                    }
                    is RequestState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else -> {} // is RequestState.Idle
                }
            }
        )
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(250.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo image "
                    )
                }
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Image(
                                painter = painterResource(R.drawable.google_logo),
                                contentDescription = "User image"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Hey Toucan")
                        }
                    },
                    selected = false,
                    onClick = { /*TODO go to user settings/account*/ }
                )
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Image(
                                painterResource(id = R.drawable.google_logo),
                                contentDescription = "Google logo"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Sign out")
                        }
                    },
                    selected = false,
                    onClick = onSignOutClicked
                )

            })
        },
        content = content
    )

}