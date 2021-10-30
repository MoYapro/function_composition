import com.github.kittinunf.result.Result


fun <T, TRANSFORMED, EXCEPTION : Exception> start(function: (T) -> Result<TRANSFORMED, EXCEPTION>): (T) -> Result<TRANSFORMED, EXCEPTION> =
    function


fun <ORIGIN, T, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<T, EXCEPTION>).then(function: (T) -> Result<TRANSFORMED, EXCEPTION>): (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { i: ORIGIN ->
        when (val intermediateResult = this.invoke(i)) {
            is Result.Success -> function(intermediateResult.get())
            is Result.Failure -> intermediateResult
        }
    }
}

fun <T, TRANSFORMED, EXCEPTION : Exception> startCatched(
    function: (T) -> Result<TRANSFORMED, EXCEPTION>,
    exception: (Exception) -> EXCEPTION,
): (T) -> Result<TRANSFORMED, EXCEPTION> =
    { originalInput ->
        try {
            function(originalInput)
        } catch (ex: Exception) {
            Result.failure(exception(ex))
        }
    }

fun <ORIGIN, T, TRANSFORMED, EXCEPTION : Exception> ((ORIGIN) -> Result<T, EXCEPTION>).thenCatched(
    function: (T) -> Result<TRANSFORMED, EXCEPTION>,
    exception: (Exception) -> EXCEPTION,
): (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> {
    return { originalInput: ORIGIN ->
        try {
            when (val intermediateResult = this.invoke(originalInput)) {
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

fun <ORIGIN, TRANSFORMED, EXCEPTION : Exception> startSuspended(
    function: suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION>,
): suspend (ORIGIN) -> Result<TRANSFORMED, EXCEPTION> = function


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
