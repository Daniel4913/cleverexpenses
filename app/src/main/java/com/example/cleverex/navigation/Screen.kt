package com.example.cleverex.navigation

import com.example.cleverex.util.Constants.BILL_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object Bill : Screen(route = "bill_screen?$BILL_SCREEN_ARGUMENT_KEY=" +
            "{$BILL_SCREEN_ARGUMENT_KEY}") {
        fun passBillId(billId: String) =
            "bill_screen?$BILL_SCREEN_ARGUMENT_KEY=$billId"
    }
}
