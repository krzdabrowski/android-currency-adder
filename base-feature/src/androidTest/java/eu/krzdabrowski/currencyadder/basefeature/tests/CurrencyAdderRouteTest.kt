package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.krzdabrowski.currencyadder.basefeature.data.generateTestRocketsFromDomain
import eu.krzdabrowski.currencyadder.basefeature.presentation.composable.CurrencyAdderRoute
import eu.krzdabrowski.currencyadder.core.MainActivity
import eu.krzdabrowski.currencyadder.core.utils.getHiltTestViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CurrencyAdderRouteTest {

    @get:Rule(order = 0)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testRockets = generateTestRocketsFromDomain()

    @Before
    fun setUp() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            CurrencyAdderRoute(
                viewModel = composeTestRule.getHiltTestViewModel(),
            )
        }
    }

    @Test
    fun rocketsRoute_whenHappyPath_shouldShowAllFakeRockets() {
        testRockets.forEach { rocket ->
            composeTestRule
                .onNodeWithText(rocket.currencyCode)
                .assertExists()
        }
    }
}
