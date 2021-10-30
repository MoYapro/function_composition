import com.github.kittinunf.result.Result

fun <ORIGIN, T, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<T, EXCEPTION>).andThen(function: (T) -> Result<TRANSFORMED, EXCEPTION>): (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}

fun <ORIGIN, OUTPUT, TRANSFORMED, EXCEPTION : Exception> (suspend (ORIGIN) -> Result<OUTPUT, EXCEPTION>).andThen(
    function: (OUTPUT) -> Result<TRANSFORMED, EXCEPTION>,
): suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}

fun <ORIGIN, OUTPUT, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<OUTPUT, EXCEPTION>).andThenSuspended(
    function: suspend (OUTPUT) -> Result<TRANSFORMED, EXCEPTION>,
): suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}
