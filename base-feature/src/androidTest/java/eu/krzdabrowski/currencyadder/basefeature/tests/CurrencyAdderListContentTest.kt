package eu.krzdabrowski.currencyadder.basefeature.tests

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import eu.krzdabrowski.currencyadder.basefeature.data.generateTestRocketsFromPresentation
import eu.krzdabrowski.currencyadder.basefeature.presentation.composable.CurrencyAdderListContent
import eu.krzdabrowski.currencyadder.basefeature.presentation.composable.USER_SAVING_DIVIDER_TEST_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyAdderListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testRockets = generateTestRocketsFromPresentation()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            CurrencyAdderListContent(
                userSavingList = testRockets,
                onUserSavingClick = { },
            )
        }
    }

    @Test
    fun rocketsListContent_shouldNotShowTheDividerAfterLastItem() {
        val expectedDividerCount = testRockets.size - 1

        composeTestRule
            .onAllNodesWithTag(USER_SAVING_DIVIDER_TEST_TAG)
            .assertCountEquals(expectedDividerCount)
    }
}
