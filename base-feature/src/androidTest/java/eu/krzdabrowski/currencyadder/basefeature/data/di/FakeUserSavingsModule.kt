package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.krzdabrowski.currencyadder.basefeature.data.dummy.generateTestUserSavingsFromDomain
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.SwapUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import kotlinx.coroutines.flow.flowOf

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserSavingsModule::class],
)
internal object FakeUserSavingsModule {

    @Provides
    fun provideFakeGetUserSavingsUseCase() = GetUserSavingsUseCase {
        flowOf(
            Result.success(generateTestUserSavingsFromDomain()),
        )
    }

    @Provides
    fun provideNoopAddUserSavingUseCase() = AddUserSavingUseCase {
        Result.success(Unit)
    }

    @Provides
    fun provideNoopUpdateUserSavingUseCase() = UpdateUserSavingUseCase {
        Result.success(Unit)
    }

    @Provides
    fun provideNoopRemoveUserSavingUseCase() = RemoveUserSavingUseCase {
        Result.success(Unit)
    }

    @Provides
    fun provideNoopSwapUserSavingsUseCase() = SwapUserSavingsUseCase { _, _ ->
        Result.success(Unit)
    }
}
