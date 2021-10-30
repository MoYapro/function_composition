import com.github.kittinunf.result.Result

fun <ORIGIN, T, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<T, EXCEPTION>).then(function: (T) -> Result<TRANSFORMED, EXCEPTION>): (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}

fun <ORIGIN, T, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<T, EXCEPTION>).thenCatched(function: (T) -> Result<TRANSFORMED, EXCEPTION>, exception: (Exception) -> EXCEPTION): (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        try {
            when (val intermediateResult = this.invoke(i)) {
                is Result.Success -> function(intermediateResult.get())
                is Result.Failure -> intermediateResult
            }
        } catch (ex: Exception) {
            Result.failure(exception(ex))
        }
    }
}

fun <ORIGIN, OUTPUT, TRANSFORMED, EXCEPTION : Exception> (suspend (ORIGIN) -> Result<OUTPUT, EXCEPTION>).then(
    function: (OUTPUT) -> Result<TRANSFORMED, EXCEPTION>,
): suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}

fun <ORIGIN, OUTPUT, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<OUTPUT, EXCEPTION>).thenSuspended(
    function: suspend (OUTPUT) -> Result<TRANSFORMED, EXCEPTION>,
): suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}
