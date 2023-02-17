package eu.krzdabrowski.currencyadder.basicfeature.domain.repository

import eu.krzdabrowski.currencyadder.basicfeature.domain.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {
    fun getExchangeRates(): Flow<List<ExchangeRate>>
    suspend fun refreshExchangeRates()
}
