package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.ExchangeRateDisplayable

fun ExchangeRate.toPresentationModel() = ExchangeRateDisplayable(
    currencyCode = currencyCode,
    currencyRate = currencyRate.toString()
)
