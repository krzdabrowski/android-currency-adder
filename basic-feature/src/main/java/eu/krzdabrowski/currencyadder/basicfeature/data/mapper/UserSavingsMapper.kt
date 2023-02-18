package eu.krzdabrowski.currencyadder.basicfeature.data.mapper

import eu.krzdabrowski.currencyadder.basicfeature.data.local.model.UserSavingCached
import eu.krzdabrowski.currencyadder.basicfeature.domain.model.UserSaving

fun UserSavingCached.toDomainModel() = UserSaving(
    id = id,
    location = location,
    saving = saving,
    currency = currency
)

fun UserSaving.toEntityModel() = UserSavingCached(
    id = id,
    location = location,
    saving = saving,
    currency = currency
)
