package eu.krzdabrowski.currencyadder.core.navigation

import kotlinx.serialization.Serializable

sealed class NavigationDestination {

    @Serializable
    data object CurrencyAdder : NavigationDestination()

    @Serializable
    data object Back : NavigationDestination()
}
