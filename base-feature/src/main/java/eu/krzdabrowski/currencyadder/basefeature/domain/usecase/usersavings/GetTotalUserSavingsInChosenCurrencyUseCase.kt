package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetTotalUserSavingsInChosenCurrencyUseCase : (String) -> Flow<Result<Double>>

fun getTotalUserSavingsInChosenCurrency(
    userSavingsRepository: UserSavingsRepository,
    currencyCode: String
): Flow<Result<Double>> = userSavingsRepository
    .getTotalUserSavingsInChosenCurrency(currencyCode)
    .map {
        resultOf { it }
    }
