package com.example.cleverex.navigation

import com.example.cleverex.util.Constants.ADD_BILL_ITEMS_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.Constants.ADD_BILL_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object AddBill : Screen(
        route = "add_bill_screen?$ADD_BILL_SCREEN_ARGUMENT_KEY=" +
                "{$ADD_BILL_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passBillId(billId: String) =
            "bill_screen?$ADD_BILL_SCREEN_ARGUMENT_KEY=$billId"
    }

    object BillOverview : Screen(
        route = "bill_overview_screen/$BILL_OVERVIEW_SCREEN_ARGUMENT_KEY=" +
                "{$BILL_OVERVIEW_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passBillId(billId: String) =
            "bill_overview_screen/$BILL_OVERVIEW_SCREEN_ARGUMENT_KEY=$billId"
    }

    object Budget : Screen(
        route = "budget_screen"
    )

    object BrowseCategories : Screen(
        route = "browse_categories_screen"
    )
}
