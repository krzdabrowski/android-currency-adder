package eu.krzdabrowski.currencyadder.basefeature.presentation

import app.cash.turbine.test
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesThatStartWithUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingPositionsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.generateTestCurrencyCodesFromDomain
import eu.krzdabrowski.currencyadder.basefeature.generateTestUserSavingsFromDomain
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.GetCurrencyCodesThatStartWith
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSavingPositions
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsViewModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toPresentationModel
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserSavingsViewModelTest {

    private val testCurrencyCodesThatStartOnLetterP = listOf("PLN", "PHP")

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    @RelaxedMockK
    private lateinit var getUserSavingsUseCase: GetUserSavingsUseCase

    @RelaxedMockK
    private lateinit var addUserSavingUseCase: AddUserSavingUseCase

    @RelaxedMockK
    private lateinit var updateUserSavingUseCase: UpdateUserSavingUseCase

    @RelaxedMockK
    private lateinit var removeUserSavingUseCase: RemoveUserSavingUseCase

    @RelaxedMockK
    private lateinit var updateUserSavingPositionsUseCase: UpdateUserSavingPositionsUseCase

    // TODO: https://github.com/mockk/mockk/issues/1073
    private lateinit var refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase

    private val getCurrencyCodesThatStartWithUseCase: GetCurrencyCodesThatStartWithUseCase =
        GetCurrencyCodesThatStartWithUseCase {
            Result.success(testCurrencyCodesThatStartOnLetterP)
        }

    @RelaxedMockK
    private lateinit var getAllCurrencyCodesUseCase: GetAllCurrencyCodesUseCase

    private lateinit var objectUnderTest: UserSavingsViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        refreshExchangeRatesUseCase = RefreshExchangeRatesUseCase {
            Result.success(Unit)
        }
    }

    @Test
    fun `should show combined user savings & currency codes during init user savings & currency codes retrieval success`() =
        runTest {
            // Given
            val testUserSavingsFromDomain = listOf(generateTestUserSavingsFromDomain())
            val testUserSavingsToPresentation = testUserSavingsFromDomain.map { it.toPresentationModel() }
            val testCurrencyCodesFromDomain = generateTestCurrencyCodesFromDomain()
            val testUserSavingsWithCurrencyCodes = testUserSavingsToPresentation.map {
                it.copy(
                    currencyPossibilities = testCurrencyCodesFromDomain,
                )
            }
            setUpUserSavingsViewModel(
                getUserSavings = flowOf(
                    Result.success(testUserSavingsFromDomain),
                ),
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
                    expected = testUserSavingsWithCurrencyCodes,
                    actual = actualItem.userSavings,
                )
            }
        }

    @Test
    fun `should show no loading & error state during init user savings & currency codes retrieval success`() = runTest {
        // Given
        val testUserSavingsFromDomain = listOf(generateTestUserSavingsFromDomain())
        val testCurrencyCodesFromDomain = generateTestCurrencyCodesFromDomain()
        setUpUserSavingsViewModel(
            getUserSavings = flowOf(
                Result.success(testUserSavingsFromDomain),
            ),
            getCurrencyCodes = flowOf(
                Result.success(testCurrencyCodesFromDomain),
            ),
        )

        // When
        // init

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertFalse(actualItem.isLoading)
            assertFalse(actualItem.isError)
        }
    }

    @Test
    fun `should show error state with no loading state during init user savings retrieval failure`() = runTest {
        // Given
        val testCurrencyCodesFromDomain = generateTestCurrencyCodesFromDomain()
        setUpUserSavingsViewModel(
            getUserSavings = flowOf(
                Result.failure(IllegalStateException("Test error")),
            ),
            getCurrencyCodes = flowOf(
                Result.success(testCurrencyCodesFromDomain),
            ),
        )

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
    fun `should show error state with no loading state during init currency codes retrieval failure`() = runTest {
        // Given
        val testUserSavingsFromDomain = listOf(generateTestUserSavingsFromDomain())
        setUpUserSavingsViewModel(
            getUserSavings = flowOf(
                Result.success(testUserSavingsFromDomain),
            ),
            getCurrencyCodes = flowOf(
                Result.failure(IllegalStateException("Test error")),
            ),
        )

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
    fun `should call proper use case with empty user saving during adding new user saving`() = runTest {
        // Given
        setUpUserSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(AddUserSaving)

        // Then
        coVerify(exactly = 1) {
            addUserSavingUseCase(any())
        }
    }

    @Test
    fun `should call proper use case with mapped user saving during updating user saving`() = runTest {
        // Given
        val testUserSavingFromDomain = generateTestUserSavingsFromDomain()
        val testUserSavingFromPresentation = testUserSavingFromDomain.toPresentationModel()
        setUpUserSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(UpdateUserSaving(testUserSavingFromPresentation))

        // Then
        coVerify(exactly = 1) {
            updateUserSavingUseCase(testUserSavingFromDomain)
        }
    }

    @Test
    fun `should call proper use case with mapped user saving during deleting user saving`() = runTest {
        // Given
        val testUserSavingFromDomain = generateTestUserSavingsFromDomain()
        val testUserSavingFromPresentation = testUserSavingFromDomain.toPresentationModel()
        setUpUserSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(RemoveUserSaving(testUserSavingFromPresentation.id))

        // Then
        coVerify(exactly = 1) {
            removeUserSavingUseCase(testUserSavingFromDomain.id)
        }
    }

    @Test
    fun `should call proper use case with proper parameters during upading user saving positions`() = runTest {
        // Given
        val movedItemId = 4L
        val fromListIndex = 0
        val toListIndex = 1

        setUpUserSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(UpdateUserSavingPositions(movedItemId, fromListIndex, toListIndex))

        // Then
        coVerify(exactly = 1) {
            updateUserSavingPositionsUseCase(movedItemId, fromListIndex, toListIndex)
        }
    }

    @Test
    fun `should update filtered currency codes only for user saving with defined test id`() = runTest {
        // Given
        val searchPhrase = "P"
        val testUserSaving = generateTestUserSavingsFromDomain()
        val testUserSavingId = 2L
        val testUserSavingsFromDomain = listOf(
            testUserSaving.copy(id = 1),
            testUserSaving.copy(id = testUserSavingId),
            testUserSaving.copy(id = 3),
        )
        val testUserSavingsFromPresentation = testUserSavingsFromDomain.map { it.toPresentationModel() }
        setUpUserSavingsViewModel(
            initialUiState = UserSavingsUiState(
                userSavings = testUserSavingsFromPresentation,
            ),
        )

        // When
        objectUnderTest.acceptIntent(GetCurrencyCodesThatStartWith(searchPhrase, testUserSavingId))

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertEquals(
                expected = emptyList(),
                actual = actualItem.userSavings[0].currencyPossibilities,
            )

            assertEquals(
                expected = testCurrencyCodesThatStartOnLetterP,
                actual = actualItem.userSavings[1].currencyPossibilities,
            )

            assertEquals(
                expected = emptyList(),
                actual = actualItem.userSavings[2].currencyPossibilities,
            )
        }
    }

    @Test
    fun `should show loading state with no error state during rockets refresh`() = runTest {
        // Given
        setUpUserSavingsViewModel()

        // When
        objectUnderTest.acceptIntent(RefreshExchangeRates)

        // Then
        objectUnderTest.uiState.test {
            val actualItem = awaitItem()

            assertTrue(actualItem.isLoading)
            assertFalse(actualItem.isError)
        }
    }

    private fun setUpUserSavingsViewModel(
        getUserSavings: Flow<Result<List<UserSaving>>> = emptyFlow(),
        getCurrencyCodes: Flow<Result<List<String>>> = emptyFlow(),
        initialUiState: UserSavingsUiState = UserSavingsUiState(),
    ) {
        every { getUserSavingsUseCase() } returns getUserSavings
        every { getAllCurrencyCodesUseCase() } returns getCurrencyCodes

        objectUnderTest = UserSavingsViewModel(
            getUserSavingsUseCase = getUserSavingsUseCase,
            addUserSavingUseCase = addUserSavingUseCase,
            updateUserSavingUseCase = updateUserSavingUseCase,
            removeUserSavingUseCase = removeUserSavingUseCase,
            updateUserSavingPositionsUseCase = updateUserSavingPositionsUseCase,
            refreshExchangeRatesUseCase = refreshExchangeRatesUseCase,
            getAllCurrencyCodesUseCase = getAllCurrencyCodesUseCase,
            getCurrencyCodesThatStartWithUseCase = getCurrencyCodesThatStartWithUseCase,
            savedStateHandle = spyk(),
            userSavingsInitialState = initialUiState,
        )
    }
}
