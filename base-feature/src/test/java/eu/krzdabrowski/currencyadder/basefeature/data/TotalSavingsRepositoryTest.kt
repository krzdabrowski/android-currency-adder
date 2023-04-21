package eu.krzdabrowski.currencyadder.basefeature.data

import app.cash.turbine.test
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.data.repository.TotalSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.core.datastore.DataStoreManager
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TotalSavingsRepositoryTest {

    @RelaxedMockK
    private lateinit var userSavingsDao: UserSavingsDao

    @RelaxedMockK
    private lateinit var exchangeRatesDao: ExchangeRatesDao

    @RelaxedMockK
    private lateinit var dataStoreManager: DataStoreManager

    private lateinit var objectUnderTest: TotalSavingsRepositoryImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        setUpTotalSavingsRepository()
    }

    @Test
    fun `should get total user savings in chosen currency`() = runTest {
        // Given
        val chosenCurrencyCodeForTotalSavings = "GBP"
        val totalSavingsInBaseCurrency = 100.0
        val exchangeRateForChosenCurrency = 5.0

        every {
            dataStoreManager.readString("CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY")
        } returns flowOf(chosenCurrencyCodeForTotalSavings)

        every {
            userSavingsDao.getTotalUserSavingsInBaseCurrency()
        } returns flowOf(totalSavingsInBaseCurrency)

        every {
            exchangeRatesDao.getExchangeRateForChosenCurrency(chosenCurrencyCodeForTotalSavings)
        } returns flowOf(exchangeRateForChosenCurrency)

        // When
        val totalSavingsFlow = objectUnderTest.getTotalUserSavings()

        // Then
        totalSavingsFlow.test {
            assertEquals(
                expected = Result.success(20.0),
                actual = awaitItem(),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should get default value of total savings if chosen currency code retrieval fails`() = runTest {
        // Given
        val chosenCurrencyCodeForTotalSavings = "GBP"
        val totalSavingsInBaseCurrency = 100.0
        val exchangeRateForChosenCurrency = 5.0

        every {
            dataStoreManager.readString("CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY")
        } throws IllegalStateException("Something went wrong")

        every {
            userSavingsDao.getTotalUserSavingsInBaseCurrency()
        } returns flowOf(totalSavingsInBaseCurrency)

        every {
            exchangeRatesDao.getExchangeRateForChosenCurrency(chosenCurrencyCodeForTotalSavings)
        } returns flowOf(exchangeRateForChosenCurrency)

        assertThrows<IllegalStateException> {
            // When
            val totalSavingsFlow = objectUnderTest.getTotalUserSavings()

            // Then
            totalSavingsFlow.test {
                assertEquals(
                    expected = Result.success(0.0),
                    actual = awaitItem(),
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private fun setUpTotalSavingsRepository() {
        objectUnderTest = TotalSavingsRepositoryImpl(
            userSavingsDao,
            exchangeRatesDao,
            dataStoreManager,
        )
    }
}
