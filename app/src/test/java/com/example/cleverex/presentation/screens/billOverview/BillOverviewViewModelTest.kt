package com.example.cleverex.presentation.screens.billOverview

import androidx.lifecycle.SavedStateHandle
import com.example.cleverex.displayable.bill.BillToDisplayableMapper
import com.example.cleverex.domain.Bill
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.util.Constants
import com.example.cleverex.util.Constants.BILL_OVERVIEW_SCREEN_ARGUMENT_KEY
import com.example.cleverex.util.RequestState
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import org.mongodb.kbson.BsonObjectId

class BillOverviewViewModelTest : StringSpec({

    //TODO logika- brak adresu? "no adress added"

    Dispatchers.setMain(Dispatchers.Unconfined) //Default
    // Unconfined nie zagwarantuje zadnej kolejnosci
    // Dla większej kontroli uzywamy StandardTestDispatcher

    "test with invalid hexadecimal" {

        val validHex = "644a6ccfc0512c56e895fa71"
        val invalidHex = "333"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Loading, RequestState.Success(Bill()))
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


        viewModel.uiState.selectedBillId shouldBe null
    }

    "test with valid bill hexadecimal " {

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Loading, RequestState.Success(data = Bill().apply {
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
        val validShop = "Lidl \uD83D\uDE04 "
        val validAddress = "Generała Karpińskiego 4, 81-173"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Loading, RequestState.Success(data = Bill().apply {
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
            with(viewModel.uiState) {
                selectedBillId shouldBe hexString
                address shouldBe validAddress
                shop shouldBe validShop
                errorMessage shouldBe null
                loading shouldBe false
            }
        }

    }

    "test with error flow" {

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(
                RequestState.Loading,
                RequestState.Error(Exception("faild to fetch billId = $hexString"))
            )
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
            viewModel.uiState.errorMessage shouldBe "faild to fetch billId = $hexString"
            viewModel.uiState.loading shouldBe false
        }

    }

    "test with loading flow" {

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns flowOf(RequestState.Loading)
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
            viewModel.uiState.loading shouldBe true
        }

    }

    "test full flow" {
        // todo 1. uiState initial loading = false
        //  2 uiState = loading
        // 3a uiState.error = requestState error
        // 3b uiState.success

        val stateList = mutableListOf<UiState>()
        // trigger jakiejs akcji
        // wrzucenie aktualnego state viewModelu do tej listy

        val stateFlow = MutableStateFlow<RequestState<Bill>>(RequestState.Loading)

        val hexString = "644a6ccfc0512c56e895fa71"

        val useCase = mockk<FetchBillUseCase> {
            coEvery {
                fetchBill(any())
            } returns stateFlow
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

        stateList.add(viewModel.uiState)
        stateFlow.emit(RequestState.Error(Exception("faild to fetch billId ")))
        stateList.add(viewModel.uiState)

        assertSoftly {
            stateList.get(0).loading
            viewModel.uiState.loading shouldBe true
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