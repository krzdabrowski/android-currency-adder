package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import eu.krzdabrowski.currencyadder.basefeature.domain.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

    fun getExchangeRates(): Flow<List<ExchangeRate>>

    suspend fun getCurrencyCodes(): List<String>

    suspend fun refreshExchangeRates()
}
