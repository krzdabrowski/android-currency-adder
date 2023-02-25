package eu.krzdabrowski.currencyadder.basefeature.presentation.mapper

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

fun UserSaving.toPresentationModel() = UserSavingDisplayable(
    id = id,
    location = location,
    saving = saving.toString(),
    currency = currency
)
