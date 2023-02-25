package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class CurrencyAdderEvent {
    data class OpenWebBrowserWithDetails(val uri: String) : CurrencyAdderEvent()
}
