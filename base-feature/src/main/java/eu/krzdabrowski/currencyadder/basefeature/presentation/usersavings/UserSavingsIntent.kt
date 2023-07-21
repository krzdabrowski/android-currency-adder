package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

sealed interface UserSavingsIntent {
    data object AddUserSaving : UserSavingsIntent

    data class UpdateUserSaving(val updatedSaving: UserSavingDisplayable) : UserSavingsIntent

    data class RemoveUserSaving(val removedUserSavingId: Long) : UserSavingsIntent

    data class SwapUserSavings(val fromListItemIndex: Int, val toListItemIndex: Int) : UserSavingsIntent

    data class GetCurrencyCodesThatStartWith(val searchPhrase: String, val userSavingId: Long) : UserSavingsIntent

    data object RefreshExchangeRates : UserSavingsIntent
}
