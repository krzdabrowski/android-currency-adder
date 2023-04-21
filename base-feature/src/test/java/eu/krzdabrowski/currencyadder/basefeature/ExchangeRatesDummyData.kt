package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.remote.model.ExchangeRatesResponse

internal fun generateTestBaseExchangeRateCached() = ExchangeRateCached(
    currencyCode = "PLN",
    currencyRate = 1.0,
)

internal fun generateTestExchangeRatesFromRemote() = ExchangeRatesResponse(
    exchangeRates = listOf(
        ExchangeRatesResponse.ExchangeRate(
            code = "USD",
            rate = 4.5276,
        ),
    ),
)

internal fun generateTestCurrencyCodesFromDomain() = listOf(
    "PLN",
    "USD",
    "EUR",
    "GBP",
)
