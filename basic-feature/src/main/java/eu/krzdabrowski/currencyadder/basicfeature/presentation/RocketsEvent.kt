package eu.krzdabrowski.currencyadder.basicfeature.presentation

sealed class RocketsEvent {
    data class OpenWebBrowserWithDetails(val uri: String) : RocketsEvent()
}
