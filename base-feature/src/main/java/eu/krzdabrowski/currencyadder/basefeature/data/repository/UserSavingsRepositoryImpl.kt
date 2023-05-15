package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
) : UserSavingsRepository {

    override fun getUserSavings(): Flow<Result<List<UserSaving>>> {
        return userSavingsDao
            .getUserSavings()
            .map { userSavings ->
                Result.success(
                    userSavings.map { it.toDomainModel() },
                )
            }
    }

    override suspend fun addUserSaving(userSaving: UserSaving): Result<Unit> = resultOf {
        userSavingsDao
            .addUserSaving(
                userSaving.toEntityModel(),
            )
    }

    override suspend fun updateUserSaving(userSaving: UserSaving): Result<Unit> = resultOf {
        userSavingsDao
            .updateUserSaving(
                userSaving.toEntityModel(),
            )
    }

    override suspend fun removeUserSaving(userSavingId: Long): Result<Unit> = resultOf {
        userSavingsDao
            .removeUserSaving(userSavingId)
    }

    override suspend fun swapUserSavings(fromIndex: Long, toIndex: Long): Result<Unit> = resultOf {
        userSavingsDao.swapUserSavings(fromIndex, toIndex)
    }
}
