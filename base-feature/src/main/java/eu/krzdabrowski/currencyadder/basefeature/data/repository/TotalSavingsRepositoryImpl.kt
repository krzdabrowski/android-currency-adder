package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.datastore.DataStoreManager
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY = "CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY"
private const val BASE_EXCHANGE_RATE_CODE = "PLN"
private const val DEFAULT_VALUE_FOR_DOUBLE = 0.0

class TotalSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
    private val exchangeRatesDao: ExchangeRatesDao,
    private val dataStoreManager: DataStoreManager,
) : TotalSavingsRepository {

    override fun getTotalUserSavings(): Flow<Result<Double>> {
        return getChosenCurrencyCodeForTotalSavings()
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
    }

    override fun getChosenCurrencyCodeForTotalSavings(): Flow<Result<String>> {
        return dataStoreManager
            .readString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY)
            .map { Result.success(it) }
    }

    override suspend fun updateChosenCurrencyCodeForTotalSavings(currencyCode: String): Result<Unit> = resultOf {
        dataStoreManager
            .writeString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY, currencyCode)
    }
}
