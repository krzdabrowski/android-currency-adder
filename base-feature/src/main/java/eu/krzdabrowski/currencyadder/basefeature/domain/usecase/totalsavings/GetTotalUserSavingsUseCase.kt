package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings

import kotlinx.coroutines.flow.Flow

fun interface GetTotalUserSavingsUseCase : () -> Flow<Result<Double>>
