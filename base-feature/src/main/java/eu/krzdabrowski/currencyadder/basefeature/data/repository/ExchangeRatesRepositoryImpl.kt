package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModels
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
    private val exchangeRatesDao: ExchangeRatesDao
) : ExchangeRatesRepository {

    override fun getExchangeRates(): Flow<List<ExchangeRate>> {
        return exchangeRatesDao
            .getExchangeRates()
            .map { exchangeRateCached ->
                exchangeRateCached.map { it.toDomainModel() }
            }
            .onEach { exchangeRates ->
                if (exchangeRates.isEmpty()) {
                    refreshExchangeRates()
                }
            }
    }

    override suspend fun refreshExchangeRates() {
        exchangeRatesApi
            .getExchangeRates()
            .toDomainModels()
            .map {
                it.toEntityModel()
            }
            .also {
                saveBaseExchangeRate()
            }
            .also { ratesToSave ->
                exchangeRatesDao.saveExchangeRates(
                    ratesToSave.sortedBy { it.currencyCode }
                )
            }
    }

    private suspend fun saveBaseExchangeRate() {
        exchangeRatesDao.saveExchangeRates(
            listOf(
                ExchangeRateCached(
                    currencyCode = BASE_EXCHANGE_RATE_CODE,
                    currencyRate = 1.0
                )
            )
        )
    }
}
