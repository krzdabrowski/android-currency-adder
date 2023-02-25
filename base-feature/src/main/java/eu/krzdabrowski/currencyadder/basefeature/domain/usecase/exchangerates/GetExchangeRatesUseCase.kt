package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

private const val RETRY_TIME_IN_MILLIS = 15_000L

fun interface GetExchangeRatesUseCase : () -> Flow<Result<List<ExchangeRate>>>

fun getExchangeRates(
    exchangeRatesRepository: ExchangeRatesRepository
): Flow<Result<List<ExchangeRate>>> = exchangeRatesRepository
    .getExchangeRates()
    .map {
        resultOf { it }
    }
    .retryWhen { cause, _ ->
        if (cause is IOException) {
            emit(Result.failure(cause))

            delay(RETRY_TIME_IN_MILLIS)
            true
        } else {
            false
        }
    }
    .catch { // for other than IOException but it will stop collecting Flow
        emit(Result.failure(it)) // also catch does re-throw CancellationException
    }
