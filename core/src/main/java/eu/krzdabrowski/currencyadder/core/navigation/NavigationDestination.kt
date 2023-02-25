package eu.krzdabrowski.currencyadder.core.navigation

sealed class NavigationDestination(
    val route: String
) {
    object CurrencyAdder : NavigationDestination("currencyAdderDestination")
    object Back : NavigationDestination("navigationBack")
}
