package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface UpdateUserSavingUseCase : suspend (UserSaving) -> Result<Unit>

suspend fun updateUserSaving(
    userSavingsRepository: UserSavingsRepository,
    userSaving: UserSaving,
): Result<Unit> = resultOf {
    userSavingsRepository.updateUserSaving(userSaving)
}
