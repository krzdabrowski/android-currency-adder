package eu.krzdabrowski.currencyadder.basefeature.data.mapper

import eu.krzdabrowski.currencyadder.basefeature.data.local.model.UserSavingCached
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

fun UserSavingCached.toDomainModel() = UserSaving(
    id = id,
    timestamp = timestamp,
    place = place,
    amount = amount,
    currency = currency,
)

fun UserSaving.toEntityModel() = UserSavingCached(
    id = id,
    timestamp = timestamp,
    place = place,
    amount = amount,
    currency = currency,
)
