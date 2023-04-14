package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

sealed interface UserSavingsIntent {
    object GetUserSavings : UserSavingsIntent

    object AddUserSaving : UserSavingsIntent

    data class UpdateUserSaving(val updatedSaving: UserSavingDisplayable) : UserSavingsIntent

    data class RemoveUserSaving(val removedSaving: UserSavingDisplayable) : UserSavingsIntent

    object RefreshExchangeRates : UserSavingsIntent

    object GetCurrencyCodes : UserSavingsIntent
}
