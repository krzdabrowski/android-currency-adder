package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

internal fun generateTestUserSavingsFromDomain() = UserSaving(
    id = 1,
    timestamp = 1684178192634L,
    place = "home",
    amount = 100.0,
    currency = "PLN",
)

internal fun generateEmptyTestUserSavingsFromDomain() = UserSaving(
    id = null,
    timestamp = 1684178192635L,
    place = "",
    amount = 0.0,
    currency = "PLN",
)
