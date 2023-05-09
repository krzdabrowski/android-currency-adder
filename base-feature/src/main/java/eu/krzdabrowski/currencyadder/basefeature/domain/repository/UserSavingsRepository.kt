package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import kotlinx.coroutines.flow.Flow

interface UserSavingsRepository {

    fun getUserSavings(): Flow<Result<List<UserSaving>>>

    suspend fun addUserSaving(userSaving: UserSaving): Result<Unit>

    suspend fun updateUserSaving(userSaving: UserSaving): Result<Unit>

    suspend fun removeUserSaving(userSaving: UserSaving): Result<Unit>

    suspend fun swapUserSavings(fromIndex: Long, toIndex: Long): Result<Unit>
}
