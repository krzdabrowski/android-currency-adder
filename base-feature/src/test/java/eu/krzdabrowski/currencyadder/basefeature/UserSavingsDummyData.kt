package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

internal fun generateTestUserSavingsFromDomain() = UserSaving(
    id = 1,
    position = 0,
    place = "home",
    amount = 100.0,
    currency = "PLN",
)
