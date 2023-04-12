package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import java.util.Locale

private const val DEFAULT_SAVING_VALUE: Double = 0.0

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    place = place,
    amount = if (amount != DEFAULT_SAVING_VALUE) {
        amount.toFormattedAmount().removeSuffix(".00")
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

fun Double.toFormattedAmount() =
    String.format(Locale.ENGLISH, "%.2f", this)
