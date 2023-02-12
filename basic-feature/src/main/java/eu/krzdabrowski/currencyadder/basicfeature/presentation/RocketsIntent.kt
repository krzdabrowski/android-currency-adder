package eu.krzdabrowski.currencyadder.basicfeature.presentation

sealed class RocketsIntent {
    object GetRockets : RocketsIntent()
    object RefreshRockets : RocketsIntent()
    data class RocketClicked(val uri: String) : RocketsIntent()
}
