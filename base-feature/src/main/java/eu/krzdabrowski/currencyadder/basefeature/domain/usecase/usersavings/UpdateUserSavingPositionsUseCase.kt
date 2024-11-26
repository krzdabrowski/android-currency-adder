package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

private typealias FromIndex = Long
private typealias ToIndex = Long

fun interface UpdateUserSavingPositionsUseCase : suspend (Long, FromIndex, ToIndex) -> Result<Unit>
