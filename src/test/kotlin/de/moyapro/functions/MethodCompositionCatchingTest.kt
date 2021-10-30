package de.moyapro.functions

import ApiInput
import DomainObject
import actionWithException
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.isFailure
import convertToDomain
import doubleTheValue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import startCatched
import then
import thenCatched
import validate

class FunctionCompositionCatchingTest {

    @Test
    fun defaultExecutionWithCatchedStart() {
        val namedExecutionPathWithCatchedStart: (DomainObject) -> Result<DomainObject, Exception> =
            startCatched(::actionWithException) { ex -> Exception("This is the mapped exception. Original message was: ${ex.message}") }
                .then(::doubleTheValue)

        val value = DomainObject(1)
        val result = namedExecutionPathWithCatchedStart(value)
        result.isFailure() shouldBe true
        (result as Result.Failure).error.message shouldContain "mapped exception"
        result.error.message shouldContain "original"
    }

    @Test
    fun executionWithCatchedException() {
        val catchingNamedExecutionPath: (ApiInput) -> Result<DomainObject, Exception> =
            ::validate
                .then(::convertToDomain)
                .thenCatched(::actionWithException) { ex -> Exception("This is the mapped exception. Original message was: ${ex.message}") }
                .then(::doubleTheValue)

        val result = catchingNamedExecutionPath(ApiInput("2"))
        result.isFailure() shouldBe true
        (result as Result.Failure).error.message shouldContain "mapped exception"
        result.error.message shouldContain "original"
    }


}

