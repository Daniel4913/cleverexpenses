package com.example.cleverex.domain

import com.example.cleverex.data.BillsRepository
import com.example.cleverex.domain.home.FetchBillUseCase
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FetchBillUseCaseTest {

    @Test
    fun test() = runTest {

        // given when then

        val repository = mockk<BillsRepository>() {
            every {
                getSelectedBill(any())
            } returns emptyFlow()
        }

        val useCase = FetchBillUseCase(repository)
        val result = useCase.fetchBill(org.mongodb.kbson.ObjectId())

        Assertions.assertFalse(result.toList().isEmpty())
    }
}


    class SomeSpec: StringSpec({

        "it should pass"{

        }

        "it should fail"{
            throw AssertionError(":(")
        }

        "it should crash"{
            throw Exception(":(((")
        }
    })
