package com.example.cleverex.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "budget_prefs")
        private val BUDGET_KEY = doublePreferencesKey("budget")
        private const val DEFAULT_BUDGET = 100.00
    }


    val budgetFlow: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[BUDGET_KEY] ?: DEFAULT_BUDGET
        }

    suspend fun saveBudget(budget: Double) {
        context.dataStore.edit { preferences ->
            preferences[BUDGET_KEY] = budget
        }
    }


}