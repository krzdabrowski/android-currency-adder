package eu.krzdabrowski.currencyadder.basicfeature.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import eu.krzdabrowski.currencyadder.basicfeature.data.local.model.ExchangeRateCached
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM Exchange_Rates")
    fun getExchangeRates(): Flow<List<ExchangeRateCached>>

    @Query("SELECT currency_code FROM Exchange_Rates")
    fun getCurrencyCodes(): Flow<List<String>>

    @Upsert
    suspend fun saveExchangeRates(exchangeRates: List<ExchangeRateCached>)
}
