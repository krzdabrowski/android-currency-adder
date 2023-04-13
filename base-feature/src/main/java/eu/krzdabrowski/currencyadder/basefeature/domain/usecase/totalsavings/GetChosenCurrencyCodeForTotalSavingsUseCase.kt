package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetChosenCurrencyCodeForTotalSavingsUseCase : () -> Flow<Result<String>>

fun getChosenCurrencyCodeForTotalSavings(
    totalSavingsRepository: TotalSavingsRepository
): Flow<Result<String>> = totalSavingsRepository
    .getChosenCurrencyCodeForTotalSavings()
    .map {
        resultOf { it }
    }
