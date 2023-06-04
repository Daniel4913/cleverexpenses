package com.example.cleverex.presentation.screens.billOverview

import com.example.cleverex.presentation.BillDisplay

data class BillOverviewScreenState(
    val billDisplay: BillDisplay,
    val loading: Boolean = false,
    val error: Throwable? = null
){

}
