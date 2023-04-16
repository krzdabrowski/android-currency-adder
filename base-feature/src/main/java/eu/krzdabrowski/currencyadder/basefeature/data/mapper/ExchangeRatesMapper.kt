package eu.krzdabrowski.currencyadder.basefeature.data.mapper

import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.remote.model.ExchangeRatesResponse
import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate

fun List<ExchangeRatesResponse>.toDomainModels() = this[0].exchangeRates.map {
    ExchangeRate(
        currencyCode = it.code,
        currencyRate = it.rate,
    )
}

fun ExchangeRate.toEntityModel() = ExchangeRateCached(
    currencyCode = currencyCode,
    currencyRate = currencyRate,
)
