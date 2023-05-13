package eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings

sealed interface TotalSavingsIntent {
    data class UpdateChosenCurrencyCodeForTotalSavings(val currencyCode: String) : TotalSavingsIntent
}
