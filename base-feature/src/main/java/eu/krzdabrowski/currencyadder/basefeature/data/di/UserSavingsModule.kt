package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.UserSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.addUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.getUserSavings
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.removeUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.updateUserSaving
import javax.inject.Singleton

@Module(includes = [UserSavingsModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object UserSavingsModule {

    @Provides
    fun provideGetUserSavingsUseCase(
        userSavingsRepository: UserSavingsRepository
    ): GetUserSavingsUseCase {
        return GetUserSavingsUseCase {
            getUserSavings(userSavingsRepository)
        }
    }

    @Provides
    fun provideAddUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
        userSaving: UserSaving
    ): AddUserSavingUseCase {
        return AddUserSavingUseCase {
            addUserSaving(userSavingsRepository, userSaving)
        }
    }

    @Provides
    fun provideUpdateUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
        userSaving: UserSaving
    ): UpdateUserSavingUseCase {
        return UpdateUserSavingUseCase {
            updateUserSaving(userSavingsRepository, userSaving)
        }
    }

    @Provides
    fun provideRemoveUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
        userSaving: UserSaving
    ): RemoveUserSavingUseCase {
        return RemoveUserSavingUseCase {
            removeUserSaving(userSavingsRepository, userSaving)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindUserSavingsRepository(impl: UserSavingsRepositoryImpl): UserSavingsRepository
    }
}
