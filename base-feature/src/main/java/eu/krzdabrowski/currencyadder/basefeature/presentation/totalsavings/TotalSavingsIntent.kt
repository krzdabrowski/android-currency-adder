package eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings

sealed interface TotalSavingsIntent {
    object GetTotalUserSavings : TotalSavingsIntent

    object GetCurrencyCodes : TotalSavingsIntent

    object GetChosenCurrencyCodeForTotalSavings : TotalSavingsIntent

    data class UpdateChosenCurrencyCodeForTotalSavings(val currencyCode: String) : TotalSavingsIntent
}
