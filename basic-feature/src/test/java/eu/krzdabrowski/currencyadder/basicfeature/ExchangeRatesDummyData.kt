package eu.krzdabrowski.currencyadder.basicfeature

import eu.krzdabrowski.currencyadder.basicfeature.data.remote.model.ExchangeRatesResponse
import eu.krzdabrowski.currencyadder.basicfeature.domain.model.ExchangeRate

internal fun generateTestExchangeRatesFromRemote() = ExchangeRatesResponse(
    exchangeRates = listOf(
        ExchangeRatesResponse.ExchangeRate(
            code = "USD",
            rate = 4.5276
        )
    ),
)

internal fun generateTestExchangeRatesFromDomain() = ExchangeRate(
    id = 1,
    currencyCode = "USD",
    currencyRate = 4.5276
)
