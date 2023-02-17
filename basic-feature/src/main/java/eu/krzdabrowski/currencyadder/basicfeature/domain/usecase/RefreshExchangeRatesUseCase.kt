package eu.krzdabrowski.currencyadder.basicfeature.domain.usecase

import eu.krzdabrowski.currencyadder.basicfeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface RefreshExchangeRatesUseCase : suspend () -> Result<Unit>

suspend fun refreshExchangeRates(
    exchangeRatesRepository: ExchangeRatesRepository
): Result<Unit> = resultOf {
    exchangeRatesRepository.refreshExchangeRates()
}
