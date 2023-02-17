package eu.krzdabrowski.currencyadder.basicfeature.data

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.krzdabrowski.currencyadder.basicfeature.data.di.ExchangeRatesModule
import eu.krzdabrowski.currencyadder.basicfeature.domain.usecase.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basicfeature.domain.usecase.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.core.extensions.resultOf
import kotlinx.coroutines.flow.flowOf

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ExchangeRatesModule::class]
)
object FakeRocketModule {

    @Provides
    fun provideFakeGetRocketsUseCase(): GetExchangeRatesUseCase {
        return GetExchangeRatesUseCase {
            flowOf(
                Result.success(generateTestRocketsFromDomain())
            )
        }
    }

    @Provides
    fun provideNoopRefreshRocketsUseCase(): RefreshExchangeRatesUseCase {
        return RefreshExchangeRatesUseCase {
            resultOf { }
        }
    }
}
