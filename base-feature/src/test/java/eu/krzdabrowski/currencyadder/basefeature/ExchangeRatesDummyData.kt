package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.remote.model.ExchangeRatesResponse
import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate

internal fun generateTestBaseExchangeRateCached() = ExchangeRateCached(
    currencyCode = "PLN",
    currencyRate = 1.0
)

internal fun generateTestExchangeRatesFromRemote() = ExchangeRatesResponse(
    exchangeRates = listOf(
        ExchangeRatesResponse.ExchangeRate(
            code = "USD",
            rate = 4.5276,
        ),
    ),
)

internal fun generateTestExchangeRatesFromDomain() = ExchangeRate(
    currencyCode = "USD",
    currencyRate = 4.5276,
)
