package eu.krzdabrowski.currencyadder.basicfeature.domain.usecase

import eu.krzdabrowski.currencyadder.basicfeature.domain.repository.RocketRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface RefreshRocketsUseCase : suspend () -> Result<Unit>

suspend fun refreshRockets(
    rocketRepository: RocketRepository
): Result<Unit> = resultOf {
    rocketRepository.refreshRockets()
}
