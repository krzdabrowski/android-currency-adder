package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings

private typealias MovedItemId = Long
private typealias FromPosition = Int
private typealias ToPosition = Int

fun interface UpdateUserSavingPositionsUseCase : suspend (MovedItemId, FromPosition, ToPosition) -> Result<Unit>
