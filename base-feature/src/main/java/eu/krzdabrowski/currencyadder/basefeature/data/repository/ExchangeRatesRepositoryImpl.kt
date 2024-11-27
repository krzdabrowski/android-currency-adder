package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModels
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.core.coroutines.IoDispatcher
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
    private val exchangeRatesDao: ExchangeRatesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ExchangeRatesRepository {

    override fun getAllCurrencyCodes(): Flow<Result<List<String>>> = exchangeRatesDao
        .getAllCurrencyCodes()
        .onEach { currencyCodes ->
            if (currencyCodes.isEmpty()) {
                refreshExchangeRates()
            }
        }
        .map { Result.success(it) }
        .distinctUntilChanged()
        .flowOn(ioDispatcher)

    override suspend fun getCurrencyCodesThatStartWith(searchPhrase: String): Result<List<String>> = resultOf {
        withContext(ioDispatcher) {
            exchangeRatesDao.getCurrencyCodesThatStartWith("$searchPhrase%")
        }
    }

    override suspend fun refreshExchangeRates(): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            exchangeRatesApi.getExchangeRates()
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
    }

    private suspend fun saveBaseExchangeRate(): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
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
}
