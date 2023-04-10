package eu.krzdabrowski.currencyadder.basefeature.domain

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.refreshExchangeRates
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class RefreshExchangeRatesUseCaseTest {

    @RelaxedMockK
    private lateinit var exchangeRatesRepository: ExchangeRatesRepository

    private lateinit var objectUnderTest: RefreshExchangeRatesUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        setUpRefreshExchangeRatesUseCase()
    }

    @Test
    fun `should wrap result with success if repository doesn't throw`() = runTest {
        // Given
        coEvery { exchangeRatesRepository.refreshExchangeRates() } just Runs

        // When
        val result = objectUnderTest.invoke()

        // Then
        assertEquals(
            expected = Result.success(Unit),
            actual = result,
        )
    }

    @Test
    fun `should rethrow if repository throws CancellationException`() = runTest {
        // Given
        coEvery { exchangeRatesRepository.refreshExchangeRates() } throws CancellationException()

        // When-Then
        assertThrows<CancellationException> {
            objectUnderTest.invoke()
        }
    }

    @Test
    fun `should wrap result with failure if repository throws other Throwable`() = runTest {
        // Given
        val testException = Throwable("Test message")
        coEvery { exchangeRatesRepository.refreshExchangeRates() } throws testException

        // When-Then
        assertThrows<Throwable> {
            val result = objectUnderTest.invoke()

            assertEquals(
                expected = Result.failure(testException),
                actual = result,
            )
        }
    }

    private fun setUpRefreshExchangeRatesUseCase() {
        objectUnderTest = RefreshExchangeRatesUseCase {
            refreshExchangeRates(exchangeRatesRepository)
        }
    }
}
