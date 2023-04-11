package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

private const val DEFAULT_SAVING_VALUE: Double = 0.0

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    place = place,
    amount = if (amount != DEFAULT_SAVING_VALUE) {
        amount.toString().removeSuffix(".0")
    } else {
        ""
    },
    currency = currency,
)

fun UserSavingDisplayable.toDomainModel() = UserSaving(
    id = id,
    place = place,
    amount = amount.toDoubleOrNull() ?: 0.0,
    currency = currency,
)
