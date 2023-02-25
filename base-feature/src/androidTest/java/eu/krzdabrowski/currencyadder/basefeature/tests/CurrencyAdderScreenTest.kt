package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.data.generateTestRocketsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.composable.CurrencyAdderScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyAdderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testRockets = generateTestRocketsFromPresentation()

    private lateinit var rocketContentDescription: String
    private lateinit var errorRefreshingMessage: String
    private lateinit var errorFetchingMessage: String

    @Before
    fun setUp() {
        with(composeTestRule.activity) {
            rocketContentDescription = getString(R.string.user_saving_content_description)
            errorRefreshingMessage = getString(R.string.exchange_rates_error_refreshing)
            errorFetchingMessage = getString(R.string.user_savings_error_fetching)
        }
    }

    @Test
    fun rocketsScreen_whenContentAvailableAndErrorOccurs_shouldKeepContent() {
        setUpComposable(
            CurrencyAdderUiState(
                userSavings = testRockets,
                isError = true
            )
        )

        composeTestRule
            .onAllNodesWithTag(rocketContentDescription)
            .assertCountEquals(testRockets.size)
    }

    @Test
    fun rocketsScreen_whenContentAvailableAndErrorOccurs_shouldShowErrorSnackbar() {
        setUpComposable(
            CurrencyAdderUiState(
                userSavings = testRockets,
                isError = true
            )
        )

        composeTestRule
            .onNodeWithText(errorRefreshingMessage)
            .assertExists()
    }

    @Test
    fun rocketsScreen_whenContentNotAvailableAndErrorOccurs_shouldHaveErrorContent() {
        setUpComposable(
            CurrencyAdderUiState(isError = true)
        )

        composeTestRule
            .onNodeWithText(errorFetchingMessage)
            .assertExists()
    }

    private fun setUpComposable(
        currencyAdderUiState: CurrencyAdderUiState
    ) {
        composeTestRule.setContent {
            CurrencyAdderScreen(
                uiState = currencyAdderUiState,
                onRefreshExchangeRates = { },
                onUserSavingCurrencyClicked = { }
            )
        }
    }
}
