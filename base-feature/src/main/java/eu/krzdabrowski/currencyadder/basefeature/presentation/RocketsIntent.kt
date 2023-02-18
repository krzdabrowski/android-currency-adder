package eu.krzdabrowski.currencyadder.basefeature.presentation

sealed class RocketsIntent {
    object GetRockets : RocketsIntent()
    object RefreshRockets : RocketsIntent()
    data class RocketClicked(val uri: String) : RocketsIntent()
}
