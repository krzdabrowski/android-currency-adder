package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving

fun interface AddUserSavingUseCase : suspend (UserSaving) -> Result<Unit>
