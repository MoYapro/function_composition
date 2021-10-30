package de.moyapro.functions

import ApiInput
import DomainObject
import com.github.kittinunf.result.Result
import convertToDomain
import doubleTheValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import start
import then
import validate

class FunctionCompositionTest {


    @Test
    fun defaultExecution() {
        val namedExecutionPath: (ApiInput) -> Result<DomainObject, Exception> =
            ::validate
                .then(::convertToDomain)
                .then(::doubleTheValue)

        val value = ApiInput("1")
        namedExecutionPath(value).get() shouldBe DomainObject(2)
    }

    @Test
    fun defaultExecutionWithStart() {
        val namedExecutionPathWithStart: (ApiInput) -> Result<DomainObject, Exception> =
            start(::validate)
                .then(::convertToDomain)
                .then(::doubleTheValue)

        val value = ApiInput("1")
        namedExecutionPathWithStart(value).get() shouldBe DomainObject(2)
    }
}
