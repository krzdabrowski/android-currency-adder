package eu.krzdabrowski.currencyadder.basefeature.domain.usecase

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface RemoveUserSavingUseCase : suspend (UserSaving) -> Result<Unit>

suspend fun removeUserSaving(
    userSavingsRepository: UserSavingsRepository,
    userSaving: UserSaving
): Result<Unit> = resultOf {
    userSavingsRepository.removeUserSaving(userSaving)
}
