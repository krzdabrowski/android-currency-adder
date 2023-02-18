package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.data.repository.ExchangeRatesRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.getExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.refreshExchangeRates
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [ExchangeRatesModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object ExchangeRatesModule {

    @Provides
    @Singleton
    fun provideExchangeRatesApi(
        retrofit: Retrofit
    ): ExchangeRatesApi {
        return retrofit.create(ExchangeRatesApi::class.java)
    }

    @Provides
    fun provideGetExchangeRatesUseCase(
        exchangeRatesRepository: ExchangeRatesRepository
    ): GetExchangeRatesUseCase {
        return GetExchangeRatesUseCase {
            getExchangeRates(exchangeRatesRepository)
        }
    }

    @Provides
    fun provideRefreshExchangeRatesUseCase(
        exchangeRatesRepository: ExchangeRatesRepository
    ): RefreshExchangeRatesUseCase {
        return RefreshExchangeRatesUseCase {
            refreshExchangeRates(exchangeRatesRepository)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindExchangeRatesRepository(impl: ExchangeRatesRepositoryImpl): ExchangeRatesRepository
    }
}
