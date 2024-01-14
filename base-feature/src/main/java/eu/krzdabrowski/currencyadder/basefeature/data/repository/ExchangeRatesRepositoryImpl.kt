package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModels
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
    private val exchangeRatesDao: ExchangeRatesDao,
) : ExchangeRatesRepository {

    override fun getAllCurrencyCodes(): Flow<Result<List<String>>> {
        return exchangeRatesDao
            .getAllCurrencyCodes()
            .onEach { currencyCodes ->
                if (currencyCodes.isEmpty()) {
                    refreshExchangeRates()
                }
            }
            .map { Result.success(it) }
    }

    override suspend fun getCurrencyCodesThatStartWith(searchPhrase: String): Result<List<String>> = resultOf {
        exchangeRatesDao
            .getCurrencyCodesThatStartWith("$searchPhrase%")
    }

    override suspend fun refreshExchangeRates(): Result<Unit> = resultOf {
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
                    ratesToSave.sortedBy { it.currencyCode },
                )
            }
    }

    private suspend fun saveBaseExchangeRate(): Result<Unit> = resultOf {
        exchangeRatesDao.saveExchangeRates(
            listOf(
                ExchangeRateCached(
                    currencyCode = BASE_EXCHANGE_RATE_CODE,
                    currencyRate = 1.0,
                ),
            ),
        )
    }
}
