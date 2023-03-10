package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.generateTestExchangeRatesFromDomain
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent.OpenWebBrowserWithDetails
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.ChooseUserSavingCurrency
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.core.utils.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CurrencyAdderViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    @RelaxedMockK
    private lateinit var getExchangeRatesUseCase: GetExchangeRatesUseCase

    // there is some issue with mocking functional interface with kotlin.Result(Unit)
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase = RefreshExchangeRatesUseCase {
        Result.failure(IllegalStateException("Test error"))
    }

    @SpyK
    private var savedStateHandle = SavedStateHandle()

    private lateinit var objectUnderTest: CurrencyAdderViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should show loading state with no error state first during init rockets retrieval`() = runTest {
        // Given
        every { getExchangeRatesUseCase() } returns emptyFlow()
        setUpRocketsViewModel()

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertTrue(actualItem.isLoading)
            assertFalse(actualItem.isError)
        }
    }

    @Test
    fun `should show fetched rockets with no loading & error state during init rockets retrieval success`() = runTest {
        // Given
        val testRocketsFromDomain = listOf(generateTestExchangeRatesFromDomain())
        val testRocketsToPresentation = testRocketsFromDomain.map { it.toPresentationModel() }
        every { getExchangeRatesUseCase() } returns flowOf(
            Result.success(testRocketsFromDomain)
        )
        setUpRocketsViewModel()

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertEquals(
                expected = testRocketsToPresentation,
                actual = actualItem.userSavings
            )
            assertFalse(actualItem.isLoading)
            assertFalse(actualItem.isError)
        }
    }

    @Test
    fun `should show error state with no loading state during init rockets retrieval failure`() = runTest {
        // Given
        every { getExchangeRatesUseCase() } returns flowOf(
            Result.failure(IllegalStateException("Test error"))
        )
        setUpRocketsViewModel()

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertTrue(actualItem.isError)
            assertFalse(actualItem.isLoading)
        }
    }

    @Test
    fun `should show error state with previously fetched rockets during rockets refresh failure`() = runTest {
        // Given
        val testRocketsFromDomain = listOf(generateTestExchangeRatesFromDomain())
        val testRocketsToPresentation = testRocketsFromDomain.map { it.toPresentationModel() }
        every { getExchangeRatesUseCase() } returns flowOf(
            Result.success(testRocketsFromDomain)
        )
        setUpRocketsViewModel()

        // When
        objectUnderTest.acceptIntent(RefreshExchangeRates)

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertTrue(actualItem.isError)
            assertEquals(
                expected = testRocketsToPresentation,
                actual = actualItem.userSavings
            )
        }
    }

    @Test
    fun `should open web browser if link has proper prefix`() = runTest {
        // Given
        val testUri = "https://testrocket.com"
        every { getExchangeRatesUseCase() } returns emptyFlow()
        setUpRocketsViewModel()

        // When
        objectUnderTest.acceptIntent(ChooseUserSavingCurrency(testUri))

        // Then
        objectUnderTest.event.test {
            assertEquals(
                expected = OpenWebBrowserWithDetails(testUri),
                actual = awaitItem()
            )
        }
    }

    @Test
    fun `should not open web browser if link is incorrect`() = runTest {
        // Given
        val testUri = "incorrectlink.com"
        every { getExchangeRatesUseCase() } returns emptyFlow()
        setUpRocketsViewModel()

        // When
        objectUnderTest.acceptIntent(ChooseUserSavingCurrency(testUri))

        // Then
        objectUnderTest.event.test {
            expectNoEvents()
        }
    }

    private fun setUpRocketsViewModel(
        initialUiState: CurrencyAdderUiState = CurrencyAdderUiState()
    ) {
        objectUnderTest = CurrencyAdderViewModel(
            getExchangeRatesUseCase,
            refreshExchangeRatesUseCase,
            savedStateHandle,
            initialUiState
        )
    }
}
