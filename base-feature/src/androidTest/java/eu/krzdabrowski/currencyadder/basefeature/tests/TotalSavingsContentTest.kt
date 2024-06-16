package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestChosenCurrencyCodeForTotalSavingsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestCurrencyCodesFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestTotalUserSavingsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.composable.TotalSavingsContent
import org.junit.Rule
import org.junit.Test

class TotalSavingsContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTotalUserSavings = generateTestTotalUserSavingsFromPresentation()
    private val testCurrencyCodes = generateTestCurrencyCodesFromPresentation()
    private val testChosenCurrencyCode = generateTestChosenCurrencyCodeForTotalSavingsFromPresentation()

    @Test
    fun totalSavingsContent_whenTotalSavingsAmountAvailable_shouldShowIt() {
        setUpComposable(
            TotalSavingsUiState(
                totalUserSavings = testTotalUserSavings,
            ),
        )

        composeTestRule
            .onNodeWithText(testTotalUserSavings)
            .assertExists()
    }

    @Test
    fun totalSavingsContent_whenChosenCurrencyCodeAvailableAndCurrencyMenuClicked_shouldShowCodeFromCurrencyList() {
        setUpComposable(
            TotalSavingsUiState(
                currencyCodes = testCurrencyCodes,
                chosenCurrencyCode = testChosenCurrencyCode,
            ),
        )

        composeTestRule
            .onNodeWithText(testChosenCurrencyCode)
            .performClick()

        composeTestRule
            .onNodeWithText("GBP")
            .assertExists()
    }

    private fun setUpComposable(totalSavingsUiState: TotalSavingsUiState) {
        composeTestRule.setContent {
            TotalSavingsContent(
                uiState = totalSavingsUiState,
                onGetTotalUserSavingsInChosenCurrency = { },
            )
        }
    }
}
