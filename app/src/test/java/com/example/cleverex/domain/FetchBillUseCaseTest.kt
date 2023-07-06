package com.example.cleverex.domain

import app.cash.turbine.testIn
import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.home.FetchBillUseCase
import com.example.cleverex.util.RequestState
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mongodb.kbson.ObjectId
import kotlin.math.exp


class FetchBillUseCaseTest {

    // fetching by id is working
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN billID WHEN fetch bill with id = billID exists THEN return that bill`() =
        runTest {
            val wrappedBill = flow<RequestState<Bill>> {
                RequestState.Success(
                    data = Bill().apply {
                        _id = ObjectId("644a6ccfc0512c56e895fa72")
                        ownerId = "6429ec6ab5591ec35eb2a0ef"
                        shop = "Lidl"
                        address = "Lidlowa 2"
                        billDate = RealmInstant.from(1683053832, 0)
                        price = 22.12
                        billItems = realmListOf(
                        )
                        billImage = ""
                        billTranscription = ""
                    }
                )
            }

            Bill().apply {
                _id = ObjectId("644a6ccfc0512c56e895fa72")
                ownerId = "6429ec6ab5591ec35eb2a0ef"
                shop = "Lidl"
                address = "Lidlowa 2"
                billDate = RealmInstant.from(1683053832, 0)
                price = 22.12
                billItems = realmListOf(
                )
                billImage = ""
                billTranscription = ""
            }




            var billId = ObjectId()

            suspend fun extractId() {
                wrappedBill.collect {
                    if (it is RequestState.Success) {
                        billId = it.data._id
                    }
                }
            }

            extractId()

            val useCase = FetchBillUseCase(
                repository = mockk {
                    coEvery {
                        // "every call in this BillsRepository"
                        getSelectedBill(eq(ObjectId("644a6ccfc0512c56e895fa72"))) // passed
//                        getSelectedBill(eq(billId)) //no answer found
                    } returns wrappedBill
                }
            )

            val expectedBill = wrappedBill
            val actualBill = useCase.fetchBill(ObjectId("644a6ccfc0512c56e895fa72"))

            assertEquals(expectedBill, actualBill)

        }

    @Test
    fun `GIVEN billID WHEN fetch bill with id = billID NOT EXISTS THEN throw exception`() =
        runTest {
            val givenId = ObjectId("644a6ccfc0512c56e895fa70")

            val useCase = FetchBillUseCase(
                repository = mockk {
                    coEvery {
                        // "every call in this BillsRepository"
                        getSelectedBill(eq(givenId))
                    } returns null
                }
            )

            val assertThrowsException = assertThrows<Exception> {
                useCase.fetchBill(givenId)
            }
            println(assertThrowsException)

//            var expectedException: Exception? = null
//
//            try {
//                useCase.fetchBill(billId = givenId)
//            } catch (e: Exception) {
//                expectedException = e
//                println(e)
//            }
//
//            assertNotNull(expectedException)


        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN billID WHEN fetch bill with id = billID NOT EXISTS THEN request state error`() =
        runTest {
            val givenId = ObjectId("644a6ccfc0512c56e895fa70")
            val expectedError = Exception("eror")
            val useCase = FetchBillUseCase(
                repository = mockk {
                    coEvery {
                        // "every call in this BillsRepository"
                        getSelectedBill(eq(givenId))
                    } returns flowOf(RequestState.Error(expectedError))
                }
            )

            val expected = useCase.fetchBill(givenId)

            expected?.collect {
                assertEquals(expected, it)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testTest() = runTest {
        // given when then

        val repository = mockk<BillsRepository>() {
            coEvery {
                getSelectedBill(any())
            } returns emptyFlow()
        }

        val useCase = FetchBillUseCase(repository)
        val result = useCase.fetchBill(org.mongodb.kbson.ObjectId())

        result?.toList()?.isEmpty()?.let { Assertions.assertFalse(it) }
    }


    // od chatgpt "testowanie z uzyciem Flow"
    // Tworzymy mocka dla BillsRepository
    private val repository: BillsRepository = mockk()

    // Inicjalizujemy FetchBillUseCase z mockowanym repozytorium
    private val fetchBillUseCase = FetchBillUseCase(repository)

    @Test
    fun `gpt`() = runBlocking {
        // Przygotowanie danych testowych
        val billId = ObjectId("644a6ccfc0512c56e895fa72")
        val expectedBill = Bill().apply {
            _id = billId
            ownerId = "6429ec6ab5591ec35eb2a0ef"
            shop = "Lidl"
            address = "Lidlowa 2"
            billDate = RealmInstant.from(1683053832, 0)
            price = 22.12
            billItems = realmListOf(
            )
            billImage = ""
            billTranscription = ""
        }


        // Mockowanie zachowania repozytorium
        coEvery { repository.getSelectedBill(billId) } returns flowOf(
            RequestState.Success(
                expectedBill
            )
        )

        // Wywołanie testowanej metody
        val flowTestObserver = fetchBillUseCase.fetchBill(ObjectId.invoke())

        // Sprawdzenie wyniku
//        flowTestObserver.assertValue(RequestState.Success(expectedBill))
        flowTestObserver?.collect { result ->
            assertEquals(RequestState.Success(expectedBill), result)
        }
    }

}


//class SomeSpec : StringSpec({
//
//    "it should pass" {
//
//    }
//
//    "it should fail" {
//        throw AssertionError(":(")
//    }
//
//    "it should crash" {
//        throw Exception(":(((")
//    }
//})
