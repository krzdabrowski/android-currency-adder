package eu.krzdabrowski.currencyadder.basefeature

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import java.util.UUID

internal fun generateTestUserSavingsFromDomain() = UserSaving(
    id = 1,
    uuid = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f1"),
    place = "home",
    amount = 100.0,
    currency = "PLN",
)
