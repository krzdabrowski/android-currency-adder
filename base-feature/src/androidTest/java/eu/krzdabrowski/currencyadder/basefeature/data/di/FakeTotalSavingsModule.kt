package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetTotalUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.UpdateChosenCurrencyCodeForTotalSavingsUseCase
import kotlinx.coroutines.flow.flowOf

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TotalSavingsModule::class],
)
internal object FakeTotalSavingsModule {

    @Provides
    fun provideNoopGetTotalUserSavingsUseCase() = GetTotalUserSavingsUseCase {
        flowOf(
            Result.success(0.0),
        )
    }

    @Provides
    fun provideNoopGetChosenCurrencyCodeForTotalSavingsUseCase() = GetChosenCurrencyCodeForTotalSavingsUseCase {
        flowOf(
            Result.success("PLN"),
        )
    }

    @Provides
    fun provideNoopUpdateChosenCurrencyCodeForTotalUserSavingsUseCase() = UpdateChosenCurrencyCodeForTotalSavingsUseCase {
        Result.success(Unit)
    }
}
