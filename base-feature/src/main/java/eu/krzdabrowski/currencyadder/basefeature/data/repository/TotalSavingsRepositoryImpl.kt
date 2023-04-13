package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private const val CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY = "CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY"
private const val DEFAULT_VALUE_FOR_DOUBLE = 0.0

class TotalSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
    private val exchangeRatesDao: ExchangeRatesDao,
    private val dataStoreManager: DataStoreManager,
) : TotalSavingsRepository {

    override fun getTotalUserSavings(): Flow<Double> {
        return getChosenCurrencyCodeForTotalSavings()
            .flatMapLatest { currencyCode ->
                combine(
                    userSavingsDao.getTotalUserSavingsInBaseCurrency(),
                    exchangeRatesDao.getExchangeRateForChosenCurrency(currencyCode)
                ) { totalUserSavingsInBaseCurrency, exchangeRateForChosenCurrency ->

                    if (exchangeRateForChosenCurrency != DEFAULT_VALUE_FOR_DOUBLE) {
                        totalUserSavingsInBaseCurrency / exchangeRateForChosenCurrency
                    } else {
                        DEFAULT_VALUE_FOR_DOUBLE
                    }
                }
            }
    }

    override fun getChosenCurrencyCodeForTotalSavings(): Flow<String> {
        return dataStoreManager.readString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY)
    }

    override suspend fun updateChosenCurrencyCodeForTotalSavings(currencyCode: String) {
        dataStoreManager.writeString(CHOSEN_CURRENCY_CODE_FOR_TOTAL_SAVINGS_KEY, currencyCode)
    }
}
