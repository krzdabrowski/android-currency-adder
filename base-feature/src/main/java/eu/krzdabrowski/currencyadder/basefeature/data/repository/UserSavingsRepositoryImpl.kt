package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
) : UserSavingsRepository {

    override fun getUserSavings(): Flow<List<UserSaving>> {
        return userSavingsDao
            .getUserSavings()
            .map { userSavings ->
                userSavings.map { it.toDomainModel() }
            }
    }

    override suspend fun addUserSaving(userSaving: UserSaving) {
        userSavingsDao
            .addUserSaving(
                userSaving.toEntityModel(),
            )
    }

    override suspend fun updateUserSaving(userSaving: UserSaving) {
        userSavingsDao
            .updateUserSaving(
                userSaving.toEntityModel(),
            )
    }

    override suspend fun removeUserSaving(userSaving: UserSaving) {
        userSavingsDao
            .removeUserSaving(
                userSaving.toEntityModel(),
            )
    }
}
