package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.repository.TotalSavingsRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetTotalUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.UpdateChosenCurrencyCodeForTotalSavingsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TotalSavingsModule {

    @Provides
    fun provideGetTotalUserSavingsUseCase(totalSavingsRepository: TotalSavingsRepository) =
        GetTotalUserSavingsUseCase(totalSavingsRepository::getTotalUserSavings)

    @Provides
    fun provideGetChosenCurrencyCodeForTotalSavingsUseCase(totalSavingsRepository: TotalSavingsRepository) =
        GetChosenCurrencyCodeForTotalSavingsUseCase(totalSavingsRepository::getChosenCurrencyCodeForTotalSavings)

    @Provides
    fun provideUpdateChosenCurrencyCodeForTotalSavingsUseCase(totalSavingsRepository: TotalSavingsRepository) =
        UpdateChosenCurrencyCodeForTotalSavingsUseCase(totalSavingsRepository::updateChosenCurrencyCodeForTotalSavings)

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindTotalSavingsRepository(impl: TotalSavingsRepositoryImpl): TotalSavingsRepository
    }
}
