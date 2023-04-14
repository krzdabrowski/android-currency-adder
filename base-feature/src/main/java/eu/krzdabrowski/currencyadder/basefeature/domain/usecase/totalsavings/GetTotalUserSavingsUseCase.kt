package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetTotalUserSavingsUseCase : () -> Flow<Result<Double>>

fun getTotalUserSavings(
    totalSavingsRepository: TotalSavingsRepository,
): Flow<Result<Double>> = totalSavingsRepository
    .getTotalUserSavings()
    .map {
        resultOf { it }
    }
