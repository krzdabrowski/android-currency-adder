package eu.krzdabrowski.currencyadder.basicfeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basicfeature.domain.model.ExchangeRate
import eu.krzdabrowski.currencyadder.basicfeature.presentation.model.ExchangeRateDisplayable

fun ExchangeRate.toPresentationModel() = ExchangeRateDisplayable(
    currencyCode = currencyCode,
    currencyRate = currencyRate.toString()
)
