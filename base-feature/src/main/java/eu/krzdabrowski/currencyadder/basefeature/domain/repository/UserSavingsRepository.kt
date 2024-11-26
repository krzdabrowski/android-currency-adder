package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import kotlinx.coroutines.flow.Flow

interface UserSavingsRepository {

    fun getUserSavings(): Flow<Result<List<UserSaving>>>

    suspend fun addUserSaving(userSaving: UserSaving): Result<Unit>

    suspend fun updateUserSaving(userSaving: UserSaving): Result<Unit>

    suspend fun updateUserSavingPositions(movedItemId: Long, fromPosition: Int, toPosition: Int): Result<Unit>

    suspend fun removeUserSaving(userSavingId: Long): Result<Unit>
}
