package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.UserSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.model.UserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.addUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.getUserSavings
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.removeUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.updateUserSaving
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
