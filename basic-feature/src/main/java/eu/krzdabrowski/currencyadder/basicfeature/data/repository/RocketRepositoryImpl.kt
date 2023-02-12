package eu.krzdabrowski.currencyadder.basicfeature.data.repository

import eu.krzdabrowski.currencyadder.basicfeature.data.local.dao.RocketDao
import eu.krzdabrowski.currencyadder.basicfeature.data.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basicfeature.data.mapper.toEntityModel
import eu.krzdabrowski.currencyadder.basicfeature.data.remote.api.RocketApi
import eu.krzdabrowski.currencyadder.basicfeature.domain.model.Rocket
import eu.krzdabrowski.currencyadder.basicfeature.domain.repository.RocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RocketRepositoryImpl @Inject constructor(
    private val rocketApi: RocketApi,
    private val rocketDao: RocketDao
) : RocketRepository {

    override fun getRockets(): Flow<List<Rocket>> {
        return rocketDao
            .getRockets()
            .map { rocketsCached ->
                rocketsCached.map { it.toDomainModel() }
            }
            .onEach { rockets ->
                if (rockets.isEmpty()) {
                    refreshRockets()
                }
            }
    }

    override suspend fun refreshRockets() {
        rocketApi
            .getRockets()
            .map {
                it.toDomainModel().toEntityModel()
            }
            .also {
                rocketDao.saveRockets(it)
            }
    }
}
