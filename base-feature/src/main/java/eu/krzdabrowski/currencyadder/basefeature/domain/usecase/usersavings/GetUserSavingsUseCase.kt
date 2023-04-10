package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetUserSavingsUseCase : () -> Flow<Result<List<UserSaving>>>

fun getUserSavings(
    userSavingsRepository: UserSavingsRepository,
): Flow<Result<List<UserSaving>>> = userSavingsRepository
    .getUserSavings()
    .map {
        resultOf { it }
    }
