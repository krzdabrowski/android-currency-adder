package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import kotlinx.coroutines.flow.Flow

interface TotalSavingsRepository {

    fun getTotalUserSavings(): Flow<Double>

    fun getChosenCurrencyCodeForTotalSavings(): Flow<String>

    suspend fun updateChosenCurrencyCodeForTotalSavings(currencyCode: String)
}
