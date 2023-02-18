package eu.krzdabrowski.currencyadder.basefeature.domain.usecase

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface GetCurrencyCodesUseCase : suspend () -> Result<List<String>>

suspend fun getCurrencyCodes(
    exchangeRatesRepository: ExchangeRatesRepository
): Result<List<String>> = resultOf {
    exchangeRatesRepository.getCurrencyCodes()
}
