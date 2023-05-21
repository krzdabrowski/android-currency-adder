package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import kotlinx.coroutines.flow.flowOf

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ExchangeRatesModule::class],
)
internal object FakeExchangeRatesModule {

    @Provides
    fun provideNoopGetAllCurrencyCodesUseCase() = GetAllCurrencyCodesUseCase {
        flowOf(
            Result.success(listOf()),
        )
    }

    @Provides
    fun provideNoopRefreshExchangeRatesUseCase() = RefreshExchangeRatesUseCase {
        Result.success(Unit)
    }
}
