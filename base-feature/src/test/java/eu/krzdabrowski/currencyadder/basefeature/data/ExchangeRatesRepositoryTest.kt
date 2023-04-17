package eu.krzdabrowski.currencyadder.basefeature.data

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModels
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.data.repository.ExchangeRatesRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.generateTestExchangeRatesFromRemote
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
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
    fun `should save sorted exchange rates locally with base exchange rate first`() = runTest {
        // Given
        val testBaseExchangeRateCached = ExchangeRateCached(
            currencyCode = "PLN",
            currencyRate = 1.0
        )
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
                listOf(testBaseExchangeRateCached)
            )
            exchangeRatesDao.saveExchangeRates(
                testExchangeRatesToCacheSorted
            )
        }
    }

    private fun setUpExchangeRatesRepository() {
        objectUnderTest = ExchangeRatesRepositoryImpl(
            exchangeRatesApi,
            exchangeRatesDao,
        )
    }
}
