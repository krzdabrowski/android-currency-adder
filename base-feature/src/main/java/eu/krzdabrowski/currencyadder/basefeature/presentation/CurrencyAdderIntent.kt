package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class CurrencyAdderIntent {
    object GetUserSavings : CurrencyAdderIntent()
    object AddUserSaving : CurrencyAdderIntent()
    data class UpdateUserSavingPlace(val newPlace: String) : CurrencyAdderIntent()
    data class UpdateUserSavingAmount(val newAmount: Double) : CurrencyAdderIntent()
    data class ChooseUserSavingCurrency(val userSavingId: Int) : CurrencyAdderIntent()

    object RefreshExchangeRates : CurrencyAdderIntent()
}
