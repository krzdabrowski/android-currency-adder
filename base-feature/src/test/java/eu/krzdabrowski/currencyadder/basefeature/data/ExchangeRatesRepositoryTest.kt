package eu.krzdabrowski.currencyadder.basefeature.data

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModels
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.data.repository.ExchangeRatesRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.generateTestBaseExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.generateTestExchangeRatesFromRemote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExchangeRatesRepositoryTest {

    @RelaxedMockK
    private lateinit var exchangeRatesApi: ExchangeRatesApi

    @RelaxedMockK
    private lateinit var exchangeRatesDao: ExchangeRatesDao

    private lateinit var objectUnderTest: ExchangeRatesRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        setUpExchangeRatesRepository()
    }

    @Test
    fun `should refresh exchange rates if local database is empty during first launch`() = runTest {
        // Given
        val testExchangeRatesFromRemote = listOf(generateTestExchangeRatesFromRemote())
        every { exchangeRatesDao.getAllCurrencyCodes() } returns flowOf(emptyList())
        coEvery { exchangeRatesApi.getExchangeRates() } returns testExchangeRatesFromRemote

        // When
        objectUnderTest.getAllCurrencyCodes().collect()

        // Then
        coVerifyOrder {
            exchangeRatesApi.getExchangeRates()
            exchangeRatesDao.saveExchangeRates(any())
        }
    }

    @Test
    fun `should save sorted exchange rates locally with base exchange rate first`() = runTest {
        // Given
        val testBaseExchangeRateCached = listOf(generateTestBaseExchangeRateCached())
        val testExchangeRatesFromRemote = listOf(generateTestExchangeRatesFromRemote())
        val testExchangeRatesToCacheSorted = testExchangeRatesFromRemote
            .toDomainModels()
            .map { it.toEntityModel() }
            .sortedBy { it.currencyCode }

        coEvery { exchangeRatesApi.getExchangeRates() } returns testExchangeRatesFromRemote

        // When
        objectUnderTest.refreshExchangeRates()

        // Then
        coVerifyOrder {
            exchangeRatesDao.saveExchangeRates(
                testBaseExchangeRateCached,
            )
            exchangeRatesDao.saveExchangeRates(
                testExchangeRatesToCacheSorted,
            )
        }
    }

    @Test
    fun `should add percentage symbol at the end of search phrase when filtering currency codes`() = runTest {
        // Given
        val testPhrase = "PL"
        val testPhraseWithPercentageSymbol = "$testPhrase%"

        // When
        objectUnderTest.getCurrencyCodesThatStartWith(testPhrase)

        // Then
        coVerify {
            exchangeRatesDao.getCurrencyCodesThatStartWith(testPhraseWithPercentageSymbol)
        }
    }

    private fun setUpExchangeRatesRepository() {
        objectUnderTest = ExchangeRatesRepositoryImpl(
            exchangeRatesApi,
            exchangeRatesDao,
        )
    }
}
