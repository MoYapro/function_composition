package de.moyapro.functions

import ApiInput
import DomainObject
import com.github.kittinunf.result.Result
import convertToDomain
import doubleTheValue
import doubleTheValueSuspended
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import startSuspended
import then
import thenSuspended
import validate

class FunctionCompositionSuspendedTest {

    @Test
    fun suspendingExecution() = runBlocking {
        val suspendedNamedExecutionPath: suspend (ApiInput) -> Result<DomainObject, Exception> =
            ::validate
                .then(::convertToDomain)
                .thenSuspended(::doubleTheValueSuspended)
                .then(::doubleTheValue)

        val value = ApiInput("1")
        val result = suspendedNamedExecutionPath(value).get()
        result shouldBe DomainObject(4)
    }

    @Test
    fun suspendingStart() = runBlocking {
        val suspendedNamedExecutionPathWithSuspendedStart: suspend (DomainObject) -> Result<DomainObject, Exception> =
            startSuspended(::doubleTheValueSuspended)
                .then(::doubleTheValue)

        val value = DomainObject(1)
        val result = suspendedNamedExecutionPathWithSuspendedStart(value).get()
        result shouldBe DomainObject(4)
    }
}

