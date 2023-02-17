package eu.krzdabrowski.currencyadder.basicfeature.data.mapper

import eu.krzdabrowski.currencyadder.basicfeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basicfeature.data.remote.model.ExchangeRatesResponse
import eu.krzdabrowski.currencyadder.basicfeature.domain.model.ExchangeRate

fun List<ExchangeRatesResponse>.toDomainModels() = this[0].exchangeRates.map {
    ExchangeRate(
        currencyCode = it.code,
        currencyRate = it.rate
    )
}

fun ExchangeRateCached.toDomainModel() = ExchangeRate(
    currencyCode = currencyCode,
    currencyRate = currencyRate
)

fun ExchangeRate.toEntityModel() = ExchangeRateCached(
    currencyCode = currencyCode,
    currencyRate = currencyRate
)
