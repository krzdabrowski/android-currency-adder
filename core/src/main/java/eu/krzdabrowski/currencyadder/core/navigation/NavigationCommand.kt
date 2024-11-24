package eu.krzdabrowski.currencyadder.core.navigation

import androidx.navigation.NavOptions

interface NavigationCommand {
    val destination: NavigationDestination
    val configuration: NavOptions
        get() = NavOptions.Builder().build()
}
