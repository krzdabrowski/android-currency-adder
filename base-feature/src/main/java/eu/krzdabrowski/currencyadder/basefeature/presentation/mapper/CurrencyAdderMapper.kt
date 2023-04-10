package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

private const val DEFAULT_SAVING_VALUE: Double = 0.0

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    place = place,
    saving = if (saving != DEFAULT_SAVING_VALUE) {
        saving.toString().removeSuffix(".0")
    } else {
        ""
    },
    currency = currency,
)

fun UserSavingDisplayable.toDomainModel() = UserSaving(
    id = id,
    place = place,
    saving = saving.toDoubleOrNull() ?: 0.0,
    currency = currency,
)
