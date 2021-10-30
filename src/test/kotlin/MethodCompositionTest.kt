import com.github.kittinunf.result.Result
import com.github.kittinunf.result.isFailure
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class FunctionCompositionTest {

    val firstNamedExecutionPath: (ApiInput) -> Result<DomainObject, Exception> =
        ::validate
            .then(::convertToDomain)
            .then(::doubleTheValue)


    val suspendedNamedExecutionPath: suspend (ApiInput) -> Result<DomainObject, Exception> =
        ::validate
            .then(::convertToDomain)
            .thenSuspended(::doubleTheValueSuspended)
            .then(::doubleTheValue)

    val catchingNamedExecutionPath: (ApiInput) -> Result<DomainObject, Exception> =
        ::validate
            .then(::convertToDomain)
            .thenCatched(::actionWithException) { ex -> Exception("This is the mapped exception. Original message was: ${ex.message}") }
            .then(::doubleTheValue)


    @Test
    fun executionWithCatchedException() {
        val result = catchingNamedExecutionPath(ApiInput("2"))
        result.isFailure() shouldBe true
        (result as Result.Failure).error.message shouldContain "mapped exception"
        result.error.message shouldContain "original"
    }

    @Test
    fun defaultExecution() {
        val value = ApiInput("1")
        firstNamedExecutionPath(value).get() shouldBe DomainObject(2)
    }

    @Test
    fun suspendingExecution() = runBlocking {
        val value = ApiInput("1")
        val result = suspendedNamedExecutionPath(value).get()
        result shouldBe DomainObject(4)
    }
}

fun validate(x: ApiInput): Result<ApiInput, Exception> {
    Result.of<Int, Exception> { x.value.toInt() }
    return Result.success(x)
}

fun convertToDomain(x: ApiInput): Result<DomainObject, Exception> {
    return Result.success(DomainObject(x.value.toInt()))
}

fun doubleTheValue(data: DomainObject): Result<DomainObject, Exception> {
    return Result.success(data.double())
}

@Suppress("UNUSED_PARAMETER")
fun actionWithException(unused: DomainObject): Result<DomainObject, Exception> {
    throw IllegalStateException("original RunTimeException")
}

@Suppress("RedundantSuspendModifier") // just to test it
suspend fun doubleTheValueSuspended(data: DomainObject): Result<DomainObject, Exception> {
    return Result.success(data.double())
}

data class ApiInput(val value: String)
data class DomainObject(val value: Int) {
    fun double(): DomainObject {
        return this.copy(value = 2 * this.value)
    }
}
