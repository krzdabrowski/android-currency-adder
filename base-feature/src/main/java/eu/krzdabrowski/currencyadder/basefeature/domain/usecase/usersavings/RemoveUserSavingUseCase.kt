package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

fun interface RemoveUserSavingUseCase : suspend (UserSaving) -> Result<Unit>
