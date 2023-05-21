package eu.krzdabrowski.currencyadder.basefeature.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRatesDao {

    @Query("SELECT code FROM Exchange_Rates")
    fun getAllCurrencyCodes(): Flow<List<String>>

    @Query(
        "SELECT code " +
            "FROM Exchange_Rates " +
            "WHERE code LIKE :searchPhraseWithWildcard",
    )
    suspend fun getCurrencyCodesThatStartWith(searchPhraseWithWildcard: String): List<String>

    @Query(
        "SELECT rates.value " +
            "FROM Exchange_Rates rates " +
            "WHERE code = :currencyCode",
    )
    fun getExchangeRateForChosenCurrency(currencyCode: String): Flow<Double>

    @Upsert
    suspend fun saveExchangeRates(exchangeRates: List<ExchangeRateCached>)
}
