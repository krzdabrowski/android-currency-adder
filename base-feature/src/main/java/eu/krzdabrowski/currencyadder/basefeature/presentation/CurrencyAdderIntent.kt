package eu.krzdabrowski.currencyadder.basefeature.presentation

import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

sealed class CurrencyAdderIntent {
    object GetUserSavings : CurrencyAdderIntent()
    object AddUserSaving : CurrencyAdderIntent()
    data class UpdateUserSaving(val updatedSaving: UserSavingDisplayable) : CurrencyAdderIntent()
    data class RemoveUserSaving(val removedSaving: UserSavingDisplayable) : CurrencyAdderIntent()

    object RefreshExchangeRates : CurrencyAdderIntent()
    object GetCurrencyCodes : CurrencyAdderIntent()

    object GetTotalUserSavings : CurrencyAdderIntent()
    object GetChosenCurrencyCodeForTotalSavings : CurrencyAdderIntent()
    data class UpdateChosenCurrencyCodeForTotalSavings(val currencyCode: String) : CurrencyAdderIntent()
}
