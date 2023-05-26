package eu.krzdabrowski.currencyadder.basefeature.data.dummy

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

internal fun generateTestUserSavingsFromPresentation() = listOf(
    UserSavingDisplayable(
        id = 1,
        timestamp = 1684178192634L,
        place = "home",
        amount = "10.00",
        currency = "PLN",
        currencyPossibilities = generateTestCurrencyCodesFromPresentation(),
    ),
    UserSavingDisplayable(
        id = 2,
        timestamp = 1684178192635L,
        place = "bank",
        amount = "20.00",
        currency = "EUR",
        currencyPossibilities = listOf("GBP"),
    ),
    UserSavingDisplayable(
        id = 3,
        timestamp = 1684178192636L,
        place = "mattress",
        amount = "30.00",
        currency = "USD",
        currencyPossibilities = generateTestCurrencyCodesFromPresentation(),
    ),
)

internal fun generateTestUserSavingsFromDomain() = listOf(
    UserSaving(
        id = 1,
        timestamp = 1684178192634L,
        place = "home",
        amount = 10.0,
        currency = "PLN",
    ),
    UserSaving(
        id = 2,
        timestamp = 1684178192635L,
        place = "bank",
        amount = 20.0,
        currency = "EUR",
    ),
    UserSaving(
        id = 3,
        timestamp = 1684178192636L,
        place = "mattress",
        amount = 30.0,
        currency = "USD",
    ),
)
