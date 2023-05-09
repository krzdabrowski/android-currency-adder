package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

fun interface SwapUserSavingsUseCase : suspend (Long, Long) -> Result<Unit>
