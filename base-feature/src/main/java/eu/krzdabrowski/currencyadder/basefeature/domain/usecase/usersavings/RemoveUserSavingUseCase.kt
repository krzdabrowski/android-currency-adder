package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

fun interface RemoveUserSavingUseCase : suspend (Long) -> Result<Unit>
