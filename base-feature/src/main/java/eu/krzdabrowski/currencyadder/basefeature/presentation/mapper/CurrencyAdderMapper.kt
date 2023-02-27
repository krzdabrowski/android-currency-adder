package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    place = place,
    saving = saving.toString(),
    currency = currency
)

fun UserSavingDisplayable.toDomainModel() = UserSaving(
    id = id,
    place = place,
    saving = saving.toDoubleOrNull() ?: 0.0,
    currency = currency
)
