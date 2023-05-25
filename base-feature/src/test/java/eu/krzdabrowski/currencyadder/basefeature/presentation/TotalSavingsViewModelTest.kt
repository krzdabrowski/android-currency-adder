package eu.krzdabrowski.currencyadder.basefeature.presentation

import app.cash.turbine.test
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetTotalUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.UpdateChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.generateTestCurrencyCodesFromDomain
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.UpdateChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsViewModel
import eu.krzdabrowski.currencyadder.core.utils.MainDispatcherExtension
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

class TotalSavingsViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    @RelaxedMockK
    private lateinit var getTotalUserSavingsUseCase: GetTotalUserSavingsUseCase

    @RelaxedMockK
    private lateinit var getAllCurrencyCodesUseCase: GetAllCurrencyCodesUseCase

    @RelaxedMockK
    private lateinit var getChosenCurrencyCodeForTotalSavingsUseCase: GetChosenCurrencyCodeForTotalSavingsUseCase

    @RelaxedMockK
    private lateinit var updateChosenCurrencyCodeForTotalSavingsUseCase: UpdateChosenCurrencyCodeForTotalSavingsUseCase

    private lateinit var objectUnderTest: TotalSavingsViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should show fetched currency codes during init currency codes retrieval success`() = runTest {
        // Given
        val testCurrencyCodesFromDomain = generateTestCurrencyCodesFromDomain()
        setUpTotalSavingsViewModel(
            getCurrencyCodes = flowOf(
                Result.success(testCurrencyCodesFromDomain),
            ),
        )

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertEquals(
                expected = testCurrencyCodesFromDomain,
                actual = actualItem.currencyCodes,
            )
        }
    }

    @Test
    fun `should show fetched formatted total savings during init total savings retrieval success`() = runTest {
        // Given
        val testTotalSavingsFromDomain = 12.3456789
        val testTotalSavingsFromPresentation = "12.35"
        setUpTotalSavingsViewModel(
            getTotalSavings = flowOf(
                Result.success(testTotalSavingsFromDomain),
            ),
        )

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertEquals(
                expected = testTotalSavingsFromPresentation,
                actual = actualItem.totalUserSavings,
            )
        }
    }

    @Test
    fun `should emit chosen currency code if not empty during init currency code retrieval success`() = runTest {
        // Given
        setUpTotalSavingsViewModel(
            getChosenCurrency = flowOf(
                Result.success("USD"),
            ),
        )

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertEquals(
                expected = "USD",
                actual = actualItem.chosenCurrencyCode,
            )
        }
    }

    @Test
    fun `should update chosen currency with base code if code is empty during init currency code retrieval success`() = runTest {
        // Given
        setUpTotalSavingsViewModel(
            getChosenCurrency = flowOf(
                Result.success(""),
            ),
        )

        // When
        // init

        // Then
        coVerify(exactly = 1) {
            updateChosenCurrencyCodeForTotalSavingsUseCase("PLN")
        }
    }

    @Test
    fun `should call proper use case during updating chosen currency code`() = runTest {
        // Given
        val testCurrencyCode = "USD"
        setUpTotalSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(UpdateChosenCurrencyCodeForTotalSavings(testCurrencyCode))

        // Then
        coVerify(exactly = 1) {
            updateChosenCurrencyCodeForTotalSavingsUseCase(testCurrencyCode)
        }
    }

    @Test
    fun `should not update ui state when error occurs`() = runTest {
        // Given
        setUpTotalSavingsViewModel(
            getChosenCurrency = flowOf(
                Result.failure(IllegalStateException("Test exception")),
            ),
        )

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val initialUiState = TotalSavingsUiState()

            assertEquals(
                expected = initialUiState,
                actual = awaitItem(),
            )
        }
    }

    private fun setUpTotalSavingsViewModel(
        getCurrencyCodes: Flow<Result<List<String>>> = emptyFlow(),
        getTotalSavings: Flow<Result<Double>> = emptyFlow(),
        getChosenCurrency: Flow<Result<String>> = emptyFlow(),
        initialUiState: TotalSavingsUiState = TotalSavingsUiState(),
    ) {
        every { getAllCurrencyCodesUseCase() } returns getCurrencyCodes
        every { getTotalUserSavingsUseCase() } returns getTotalSavings
        every { getChosenCurrencyCodeForTotalSavingsUseCase() } returns getChosenCurrency

        objectUnderTest = TotalSavingsViewModel(
            getTotalUserSavingsUseCase = getTotalUserSavingsUseCase,
            getAllCurrencyCodesUseCase = getAllCurrencyCodesUseCase,
            getChosenCurrencyCodeForTotalSavingsUseCase = getChosenCurrencyCodeForTotalSavingsUseCase,
            updateChosenCurrencyCodeForTotalSavingsUseCase = updateChosenCurrencyCodeForTotalSavingsUseCase,
            savedStateHandle = spyk(),
            totalSavingsInitialState = initialUiState,
        )
    }
}
