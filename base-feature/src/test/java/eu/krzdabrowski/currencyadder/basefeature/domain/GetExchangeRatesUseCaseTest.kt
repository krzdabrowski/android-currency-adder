package eu.krzdabrowski.currencyadder.basefeature.domain

import app.cash.turbine.test
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.getExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.generateTestExchangeRatesFromDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertEquals

class GetExchangeRatesUseCaseTest {

    @RelaxedMockK
    private lateinit var exchangeRatesRepository: ExchangeRatesRepository

    private lateinit var objectUnderTest: GetExchangeRatesUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        setUpGetExchangeRatesUseCase()
    }

    @Test
    fun `should wrap result with success if repository doesn't throw`() = runTest {
        // Given
        val testExchangeRatesFromDomain = listOf(generateTestExchangeRatesFromDomain())
        coEvery { exchangeRatesRepository.getExchangeRates() } returns flowOf(testExchangeRatesFromDomain)

        // When-Then
        objectUnderTest.invoke().test {
            val result = awaitItem()

            assertEquals(
                expected = Result.success(testExchangeRatesFromDomain),
                actual = result
            )
            awaitComplete()
        }
    }

    @Test
    fun `should retry operation if repository throws IOException`() = runTest {
        // Given
        val testException = IOException("Test message")
        val testExchangeRatesFromDomain = listOf(generateTestExchangeRatesFromDomain())
        coEvery { exchangeRatesRepository.getExchangeRates() } throws testException andThen flowOf(testExchangeRatesFromDomain)

        // When-Then
        assertThrows<IOException> {
            objectUnderTest.invoke().test {
                val errorResult = awaitItem()

                assertEquals(
                    expected = Result.failure(testException),
                    actual = errorResult
                )

                val itemsResult = awaitItem()

                assertEquals(
                    expected = Result.success(testExchangeRatesFromDomain),
                    actual = itemsResult
                )
            }
        }
    }

    @Test
    fun `should rethrow if repository throws CancellationException`() = runTest {
        // Given
        coEvery { exchangeRatesRepository.getExchangeRates() } throws CancellationException()

        // When-Then
        assertThrows<CancellationException> {
            objectUnderTest.invoke()
        }
    }

    @Test
    fun `should wrap result with failure if repository throws other Exception`() = runTest {
        // Given
        val testException = Exception("Test message")
        coEvery { exchangeRatesRepository.getExchangeRates() } throws testException

        // When-Then
        assertThrows<Exception> {
            objectUnderTest.invoke().test {
                val result = awaitItem()

                assertEquals(
                    expected = Result.failure(testException),
                    actual = result
                )
            }
        }
    }

    private fun setUpGetExchangeRatesUseCase() {
        objectUnderTest = GetExchangeRatesUseCase {
            getExchangeRates(exchangeRatesRepository)
        }
    }
}
