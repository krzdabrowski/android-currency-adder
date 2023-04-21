package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

internal fun generateTestUserSavingsFromDomain() = UserSaving(
    id = 1,
    place = "home",
    amount = 100.0,
    currency = "PLN",
)

internal fun generateEmptyTestUserSavingsFromDomain() = UserSaving(
    id = 0,
    place = "",
    amount = 0.0,
    currency = "PLN",
)
