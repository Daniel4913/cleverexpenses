package com.example.cleverex.presentation.screens.billOverview

import androidx.lifecycle.SavedStateHandle
import com.example.cleverex.displayable.bill.BillToDisplayableMapper
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.util.Constants
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import io.kotest.assertions.any
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.mongodb.kbson.BsonObjectId

class BillOverviewViewModelTest : StringSpec({

    //TODO logika- brak adresu? "no adress added"
    //TODO updateDateTime - to sie bierze skadś z zewnątrz - co sie stanie kiedy wywolam ten update wywołam

    //TODO PYTANIE -

    Dispatchers.setMain(Dispatchers.Unconfined) //Default
    // Unconfined nie zagwarantuje zadnej kolejnosci
    // Dla większej kontroli uzywamy StandardTestDispatcher

    "test with invalid hexadecimal" {


        val validHex = "644a6ccfc0512c56e895fa71"
        val invalidHex = "333"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Success(Bill()))
        }

        val savedStateHandle = mockk<SavedStateHandle> {
            every {
                get<String>(BILL_OVERVIEW_SCREEN_ARGUMENT_KEY)
            } returns invalidHex
        }

        val viewModel = createViewModel(
            fetchBillUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        // to jeszcze co innego z argument key
        viewModel.uiState.selectedBillId shouldNotBe validHex
    }

    "test with valid bill hex " {

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Success(data = Bill().apply {
                _id = BsonObjectId("644a6ccfc0512c56e895fa71")
            }))
        }

        val savedStateHandle = mockk<SavedStateHandle> {
            every {
                get<String>(Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY)
            } returns hexString
        }

        val viewModel = createViewModel(
            fetchBillUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        viewModel.uiState.selectedBillId shouldBe hexString

    }

    "test with valid bill properties" {

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Success(data = Bill().apply {
                _id = BsonObjectId("644a6ccfc0512c56e895fa71")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl \uD83D\uDE04 "
                address = "Generała Karpińskiego 4, 81-173" //2023-05-27
                billDate = RealmInstant.from(1682967432, 0)
                price = 11.11
                billItems = realmListOf()
                billImage = ""
                billTranscription = ""
            }))
        }

        val savedStateHandle = mockk<SavedStateHandle> {
            every {
                get<String>(Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY)
            } returns hexString
        }

        val viewModel = createViewModel(
            fetchBillUseCase = useCase,
            savedStateHandle = savedStateHandle
        )

        assertSoftly {
            viewModel.uiState.selectedBillId shouldBe hexString
            viewModel.uiState.address shouldBe hexString

        }

    }
})

fun createViewModel(
    fetchBillUseCase: FetchBillUseCase = mockk(),
    displayableMapper: BillToDisplayableMapper = BillToDisplayableMapper(),
    savedStateHandle: SavedStateHandle = SavedStateHandle()
): BillOverviewViewModel {
    return BillOverviewViewModel(
        fetchBillUseCase = fetchBillUseCase,
        displayableMapper = displayableMapper,
        savedStateHandle = savedStateHandle
    )
}