package eu.krzdabrowski.currencyadder.basefeature.data.dummy

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import java.util.UUID

internal fun generateTestUserSavingsFromPresentation() = listOf(
    UserSavingDisplayable(
        id = 1,
        uuid = "e58ed763-928c-4155-bee9-fdbaaadc15f1",
        place = "home",
        amount = "10.00",
        currency = "PLN",
        currencyPossibilities = generateTestCurrencyCodesFromPresentation(),
    ),
    UserSavingDisplayable(
        id = 2,
        uuid = "e58ed763-928c-4155-bee9-fdbaaadc15f2",
        place = "bank",
        amount = "20.00",
        currency = "EUR",
        currencyPossibilities = listOf("GBP"),
    ),
    UserSavingDisplayable(
        id = 3,
        uuid = "e58ed763-928c-4155-bee9-fdbaaadc15f3",
        place = "mattress",
        amount = "30.00",
        currency = "USD",
        currencyPossibilities = generateTestCurrencyCodesFromPresentation(),
    ),
)

internal fun generateTestUserSavingsFromDomain() = listOf(
    UserSaving(
        id = 1,
        uuid = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f1"),
        place = "home",
        amount = 10.0,
        currency = "PLN",
    ),
    UserSaving(
        id = 2,
        uuid = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f2"),
        place = "bank",
        amount = 20.0,
        currency = "EUR",
    ),
    UserSaving(
        id = 3,
        uuid = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        place = "mattress",
        amount = 30.0,
        currency = "USD",
    ),
)
