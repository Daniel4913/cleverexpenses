package com.example.cleverex.presentation.screens.billOverview

import com.example.cleverex.displayable.BillDisplayable

data class BillOverviewScreenState(
    val billDisplayable: BillDisplayable,
    val loading: Boolean = false,
    val error: Throwable? = null
){

}
