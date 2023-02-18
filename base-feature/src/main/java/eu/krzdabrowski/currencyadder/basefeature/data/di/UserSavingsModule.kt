package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.UserSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import javax.inject.Singleton

@Module(includes = [UserSavingsModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object UserSavingsModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindUserSavingsRepository(impl: UserSavingsRepositoryImpl): UserSavingsRepository
    }
}
