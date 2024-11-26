package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.UserSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingPositionsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UserSavingsModule {

    @Provides
    fun provideGetUserSavingsUseCase(userSavingsRepository: UserSavingsRepository) =
        GetUserSavingsUseCase(userSavingsRepository::getUserSavings)

    @Provides
    fun provideAddUserSavingUseCase(userSavingsRepository: UserSavingsRepository) =
        AddUserSavingUseCase(userSavingsRepository::addUserSaving)

    @Provides
    fun provideUpdateUserSavingUseCase(userSavingsRepository: UserSavingsRepository) =
        UpdateUserSavingUseCase(userSavingsRepository::updateUserSaving)

    @Provides
    fun provideRemoveUserSavingUseCase(userSavingsRepository: UserSavingsRepository) =
        RemoveUserSavingUseCase(userSavingsRepository::removeUserSaving)

    @Provides
    fun provideUpdateUserSavingPositionsUseCase(userSavingsRepository: UserSavingsRepository) =
        UpdateUserSavingPositionsUseCase(userSavingsRepository::updateUserSavingPositions)

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindUserSavingsRepository(impl: UserSavingsRepositoryImpl): UserSavingsRepository
    }
}
