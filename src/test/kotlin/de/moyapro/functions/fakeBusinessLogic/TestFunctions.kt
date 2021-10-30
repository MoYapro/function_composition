import com.github.kittinunf.result.Result

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
