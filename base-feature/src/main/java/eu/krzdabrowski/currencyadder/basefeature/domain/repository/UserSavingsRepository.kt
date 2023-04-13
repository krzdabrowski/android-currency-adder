package eu.krzdabrowski.currencyadder.basefeature.domain.repository

import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import kotlinx.coroutines.flow.Flow

interface UserSavingsRepository {

    fun getUserSavings(): Flow<List<UserSaving>>

    suspend fun addUserSaving(userSaving: UserSaving)

    suspend fun updateUserSaving(userSaving: UserSaving)

    suspend fun removeUserSaving(userSaving: UserSaving)
}
