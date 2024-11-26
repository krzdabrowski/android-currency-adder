package eu.krzdabrowski.currencyadder.basefeature.data.repository

import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.core.coroutines.IoDispatcher
import eu.krzdabrowski.currencyadder.core.utils.resultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserSavingsRepositoryImpl @Inject constructor(
    private val userSavingsDao: UserSavingsDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserSavingsRepository {

    override fun getUserSavings(): Flow<Result<List<UserSaving>>> = userSavingsDao
        .getUserSavings()
        .map { userSavings ->
            Result.success(userSavings.map { it.toDomainModel() })
        }
        .flowOn(ioDispatcher)

    override suspend fun addUserSaving(userSaving: UserSaving): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            userSavingsDao.addUserSaving(userSaving.toEntityModel())
        }
    }

    override suspend fun updateUserSaving(userSaving: UserSaving): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            userSavingsDao.updateUserSaving(userSaving.toEntityModel())
        }
    }

    override suspend fun removeUserSaving(userSavingId: Long): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            userSavingsDao.removeUserSaving(userSavingId)
        }
    }

    override suspend fun updateUserSavingPositions(
        movedItemId: Long,
        fromPosition: Int,
        toPosition: Int,
    ): Result<Unit> = resultOf {
        withContext(ioDispatcher) {
            userSavingsDao.updateUserSavingPositions(movedItemId, fromPosition, toPosition)
        }
    }
}
