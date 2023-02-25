package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class CurrencyAdderIntent {
    object GetRockets : CurrencyAdderIntent()
    object RefreshRockets : CurrencyAdderIntent()
    data class RocketClicked(val uri: String) : CurrencyAdderIntent()
}
