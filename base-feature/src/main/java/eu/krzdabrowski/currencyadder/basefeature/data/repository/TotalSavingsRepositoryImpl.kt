package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.coroutines.IoDispatcher
import eu.krzdabrowski.currencyadder.core.datastore.DataStoreManager
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY = "CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY"
private const val BASE_EXCHANGE_RATE_CODE = "PLN"
private const val DEFAULT_VALUE_FOR_DOUBLE = 0.0

class TotalSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
    private val exchangeRatesDao: ExchangeRatesDao,
    private val dataStoreManager: DataStoreManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TotalSavingsRepository {

    override fun getTotalUserSavings(): Flow<Result<Double>> = getChosenCurrencyCodeForTotalSavings()
        .flatMapLatest { currencyCode ->
            combine(
                userSavingsDao.getTotalUserSavingsInBaseCurrency(),
                exchangeRatesDao.getExchangeRateForChosenCurrency(
                    currencyCode.getOrDefault(BASE_EXCHANGE_RATE_CODE),
                ),
            ) { totalUserSavingsInBaseCurrency, exchangeRateForChosenCurrency ->

                if (exchangeRateForChosenCurrency != DEFAULT_VALUE_FOR_DOUBLE) {
                    totalUserSavingsInBaseCurrency / exchangeRateForChosenCurrency
                } else {
                    DEFAULT_VALUE_FOR_DOUBLE
                }
            }.map { Result.success(it) }
        }
        .distinctUntilChanged()
        .flowOn(ioDispatcher)

    override fun getChosenCurrencyCodeForTotalSavings(): Flow<Result<String>> = dataStoreManager
        .readString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY)
        .map { Result.success(it) }
        .flowOn(ioDispatcher)

    override suspend fun updateChosenCurrencyCodeForTotalSavings(currencyCode: String): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            dataStoreManager.writeString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY, currencyCode)
        }
    }
}
