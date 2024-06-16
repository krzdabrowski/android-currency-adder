package eu.krzdabrowski.currencyadder.core.navigation

sealed class NavigationDestination(
    val route: String,
) {
    data object CurrencyAdder : NavigationDestination("currencyAdderDestination")

    data object Back : NavigationDestination("navigationBack")
}
