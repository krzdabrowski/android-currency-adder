package eu.krzdabrowski.currencyadder.basefeature.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM Exchange_Rates")
    fun getExchangeRates(): Flow<List<ExchangeRateCached>>

    @Query("SELECT code FROM Exchange_Rates")
    suspend fun getCurrencyCodes(): List<String>

    @Upsert
    suspend fun saveExchangeRates(exchangeRates: List<ExchangeRateCached>)
}
