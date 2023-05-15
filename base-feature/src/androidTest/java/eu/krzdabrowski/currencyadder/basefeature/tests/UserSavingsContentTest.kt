package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestCurrencyCodesFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestUserSavingsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable.UserSavingsContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val headerStrings = listOf(
    "Place",
    "Saving",
    "Currency",
)

class UserSavingsContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testUserSavings = generateTestUserSavingsFromPresentation()
    private val testCurrencyCodes = generateTestCurrencyCodesFromPresentation()

    private lateinit var userSavingContentDescription: String
    private lateinit var userSavingsEmptyMessage: String
    private lateinit var errorRefreshingMessage: String

    @Before
    fun setUp() {
        with(composeTestRule.activity) {
            userSavingContentDescription = getString(R.string.user_saving_content_description)
            userSavingsEmptyMessage = getString(R.string.list_empty_message)
            errorRefreshingMessage = getString(R.string.exchange_rates_error_refreshing)
        }
    }

    @Test
    fun userSavingsContent_whenContentAvailable_shouldShowTheHeader() {
        setUpComposable(
            UserSavingsUiState(
                userSavings = testUserSavings,
            ),
        )

        headerStrings.forEach {
            composeTestRule
                .onNodeWithText(it)
                .assertExists()
        }
    }

    @Test
    fun userSavingsContent_whenContentAvailableAndErrorOccurs_shouldKeepContent() {
        setUpComposable(
            UserSavingsUiState(
                userSavings = testUserSavings,
                isError = true,
            ),
        )

        composeTestRule
            .onAllNodesWithTag(userSavingContentDescription)
            .assertCountEquals(testUserSavings.size)
    }

    @Test
    fun userSavingsContent_whenContentAvailableAndErrorOccurs_shouldShowErrorSnackbar() {
        setUpComposable(
            UserSavingsUiState(
                userSavings = testUserSavings,
                isError = true,
            ),
        )

        composeTestRule
            .onNodeWithText(errorRefreshingMessage)
            .assertExists()
    }

    @Test
    fun userSavingsContent_whenContentAvailableAndCurrencyMenuClicked_shouldShowCodeFromCurrencyList() {
        setUpComposable(
            UserSavingsUiState(
                userSavings = testUserSavings,
                currencyCodes = testCurrencyCodes,
            ),
        )

        composeTestRule
            .onNodeWithText(testUserSavings[1].currency)
            .performClick()

        composeTestRule
            .onNodeWithText("GBP")
            .assertExists()
    }

    @Test
    fun userSavingsContent_whenContentNotAvailable_shouldShowEmptyListMessage() {
        setUpComposable(
            UserSavingsUiState(),
        )

        composeTestRule
            .onNodeWithText(userSavingsEmptyMessage)
            .assertExists()
    }

    private fun setUpComposable(
        userSavingsUiState: UserSavingsUiState,
    ) {
        composeTestRule.setContent {
            UserSavingsContent(
                uiState = userSavingsUiState,
                onAddUserSaving = { },
                onUpdateUserSaving = { },
                onRemoveUserSaving = { },
                onDragAndDropUserSaving = { _, _ -> },
                onRefreshExchangeRates = { },
            )
        }
    }
}
