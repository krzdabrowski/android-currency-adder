package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestUserSavingsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable.UserSavingsListContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val headerStrings = listOf(
    "Place",
    "Saving",
    "Currency",
)

class UserSavingsListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testUserSavings = generateTestUserSavingsFromPresentation()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            UserSavingsListContent(
                uiState = UserSavingsUiState(
                    userSavings = testUserSavings,
                ),
                onUpdateUserSaving = { },
                onRemoveUserSaving = { },
            )
        }
    }

    @Test
    fun userSavingsListContent_shouldShowTheHeader() {
        headerStrings.forEach {
            composeTestRule
                .onNodeWithText(it)
                .assertExists()
        }
    }
}
