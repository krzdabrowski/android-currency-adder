package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.utils.toFormattedAmount
import java.util.UUID

private const val DEFAULT_SAVING_VALUE: Double = 0.0

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    uuid = uuid.toString(),
    place = place,
    amount = if (amount != DEFAULT_SAVING_VALUE) {
        amount.toFormattedAmount().removeSuffix(".00")
    } else {
        ""
    },
    currency = currency,
    currencyPossibilities = emptyList(),
)

fun UserSavingDisplayable.toDomainModel() = UserSaving(
    id = id,
    uuid = UUID.fromString(uuid),
    place = place,
    amount = amount.toDoubleOrNull() ?: 0.0,
    currency = currency,
)
