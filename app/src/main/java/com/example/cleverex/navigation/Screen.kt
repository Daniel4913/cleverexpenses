package com.example.cleverex.navigation

import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object AddBill : Screen(route = "add_bill_screen")
    object BillOverview : Screen(
        route = "bill_overview_screen/{$BILL_OVERVIEW_SCREEN_ARGUMENT_KEY}"
    ){
        fun passBillId(billId: String){
            "bill_overview_screen/{$BILL_OVERVIEW_SCREEN_ARGUMENT_KEY}=$billId"
        }
    }
}

