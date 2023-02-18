package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class RocketsEvent {
    data class OpenWebBrowserWithDetails(val uri: String) : RocketsEvent()
}
