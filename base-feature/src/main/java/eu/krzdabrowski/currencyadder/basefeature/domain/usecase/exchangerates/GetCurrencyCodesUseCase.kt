package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetCurrencyCodesUseCase : () -> Flow<Result<List<String>>>

fun getCurrencyCodes(
    exchangeRatesRepository: ExchangeRatesRepository,
): Flow<Result<List<String>>> =
    exchangeRatesRepository.getCurrencyCodes()
        .map {
            resultOf { it }
        }
