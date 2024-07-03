package eu.krzdabrowski.currencyadder.basefeature.data.mapper

import eu.krzdabrowski.currencyadder.basefeature.data.local.model.UserSavingCached
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import java.util.UUID

fun UserSavingCached.toDomainModel() = UserSaving(
    id = id,
    uuid = UUID.fromString(uuid),
    place = place,
    amount = amount,
    currency = currency,
)

fun UserSaving.toEntityModel() = UserSavingCached(
    id = id,
    uuid = uuid.toString(),
    place = place,
    amount = amount,
    currency = currency,
)
