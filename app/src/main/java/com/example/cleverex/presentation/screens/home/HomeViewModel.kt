package com.example.cleverex.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleverex.data.Bills
import com.example.cleverex.data.FakeBillsDb
import com.example.cleverex.data.MongoDB
import com.example.cleverex.util.RequestState

import kotlinx.coroutines.launch



class HomeViewModel (
//    private val fakeBillsDb: FakeBillsDb,
//    private val mongoDB: MongoDB
) : ViewModel() {

    var bills: MutableState<Bills> = mutableStateOf(RequestState.Idle)
    var fakeBills: MutableState<Bills> = mutableStateOf(RequestState.Idle)

    init {
        observeFakeBills()
        observeAllBills()
    }

    private fun observeAllBills() {
        viewModelScope.launch {
            MongoDB.getAllBills().collect { result ->
                bills.value = result
                Log.d("vM observeAllBills","$result")
            }
        }
    }

    private fun observeFakeBills() {
        viewModelScope.launch {
            FakeBillsDb.getAllFakeBills().collect { result ->
                fakeBills.value = result
                Log.d("vM observeFakeBills","$result")
            }
        }
    }

}