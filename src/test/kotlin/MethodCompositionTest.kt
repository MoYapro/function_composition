import com.github.kittinunf.result.Result
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ArrowTest {

    val firstNamedExecutionPath: (ApiInput) -> Result<DomainObject, Exception> =
        ::validate
            .andThen(::convertToDomain)
            .andThen(::doDomainLogic)


    val suspendedNamedExecutionPath: suspend (ApiInput) -> Result<DomainObject, Exception> =
        ::validate
            .andThen(::convertToDomain)
            .andThenSuspended(::doSuspendedDomainLogic)
            .andThen(::doDomainLogic)

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

fun doDomainLogic(data: DomainObject): Result<DomainObject, Exception> {
    return Result.success(data.double())
}

@Suppress("RedundantSuspendModifier") // just to test it
suspend fun doSuspendedDomainLogic(data: DomainObject): Result<DomainObject, Exception> {
    return Result.success(data.double())
}

data class ApiInput(val value: String)
data class DomainObject(val value: Int) {
    fun double(): DomainObject {
        return this.copy(value = 2 * this.value)
    }
}
