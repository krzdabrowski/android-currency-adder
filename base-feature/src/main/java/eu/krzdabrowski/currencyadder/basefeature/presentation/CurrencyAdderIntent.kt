package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class CurrencyAdderIntent {
    object GetUserSavings : CurrencyAdderIntent()
    object RefreshExchangeRates : CurrencyAdderIntent()
    data class UserSavingLocationChanged(val newLocation: String) : CurrencyAdderIntent()
    data class UserSavingAmountChanged(val newAmount: Double) : CurrencyAdderIntent()
    data class UserSavingCurrencyClicked(val userSavingId: Int) : CurrencyAdderIntent()
}
