package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.UserSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.UserSavingsRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetTotalUserSavingsInChosenCurrencyUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.addUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.getTotalUserSavingsInChosenCurrency
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.getUserSavings
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.removeUserSaving
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.updateUserSaving
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UserSavingsModule {

    @Provides
    fun provideGetUserSavingsUseCase(
        userSavingsRepository: UserSavingsRepository,
    ): GetUserSavingsUseCase {
        return GetUserSavingsUseCase {
            getUserSavings(userSavingsRepository)
        }
    }

    @Provides
    fun provideGetTotalUserSavingsInChosenCurrencyUseCase(
        userSavingsRepository: UserSavingsRepository
    ): GetTotalUserSavingsInChosenCurrencyUseCase {
        return GetTotalUserSavingsInChosenCurrencyUseCase {
            getTotalUserSavingsInChosenCurrency(
                userSavingsRepository,
                it
            )
        }
    }

    @Provides
    fun provideAddUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
    ): AddUserSavingUseCase {
        return AddUserSavingUseCase {
            addUserSaving(userSavingsRepository, it)
        }
    }

    @Provides
    fun provideUpdateUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
    ): UpdateUserSavingUseCase {
        return UpdateUserSavingUseCase {
            updateUserSaving(userSavingsRepository, it)
        }
    }

    @Provides
    fun provideRemoveUserSavingUseCase(
        userSavingsRepository: UserSavingsRepository,
    ): RemoveUserSavingUseCase {
        return RemoveUserSavingUseCase {
            removeUserSaving(userSavingsRepository, it)
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
