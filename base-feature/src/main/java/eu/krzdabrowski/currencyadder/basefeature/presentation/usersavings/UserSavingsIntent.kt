package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

sealed interface UserSavingsIntent {
    object AddUserSaving : UserSavingsIntent

    data class UpdateUserSaving(val updatedSaving: UserSavingDisplayable) : UserSavingsIntent

    data class RemoveUserSaving(val removedSaving: UserSavingDisplayable) : UserSavingsIntent

    data class SwapUserSavings(val fromListItemIndex: Int, val toListItemIndex: Int) : UserSavingsIntent

    object RefreshExchangeRates : UserSavingsIntent
}
