package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.krzdabrowski.currencyadder.basefeature.presentation.composable.RocketsRoute
import eu.krzdabrowski.currencyadder.core.navigation.NavigationDestination.Rockets
import eu.krzdabrowski.currencyadder.core.navigation.NavigationFactory
import javax.inject.Inject

class RocketsNavigationFactory @Inject constructor() : NavigationFactory {

    override fun create(builder: NavGraphBuilder) {
        builder.composable(Rockets.route) {
            RocketsRoute()
        }
    }
}
