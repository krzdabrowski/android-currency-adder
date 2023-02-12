package eu.krzdabrowski.currencyadder.core.navigation

sealed class NavigationDestination(
    val route: String
) {
    object Rockets : NavigationDestination("rocketsDestination")
    object Back : NavigationDestination("navigationBack")
}
