package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import kotlinx.coroutines.flow.Flow

fun interface GetUserSavingsUseCase : () -> Flow<Result<List<UserSaving>>>
