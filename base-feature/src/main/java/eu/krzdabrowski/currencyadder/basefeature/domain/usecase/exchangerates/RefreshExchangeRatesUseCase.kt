package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface RefreshExchangeRatesUseCase : suspend () -> Result<Unit>

suspend fun refreshExchangeRates(
    exchangeRatesRepository: ExchangeRatesRepository,
): Result<Unit> = resultOf {
    exchangeRatesRepository.refreshExchangeRates()
}
