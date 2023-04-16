package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

    fun getCurrencyCodes(): Flow<Result<List<String>>>

    suspend fun refreshExchangeRates(): Result<Unit>
}
