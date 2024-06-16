package eu.krzdabrowski.currencyadder.basefeature.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.remote.api.ExchangeRatesApi
import eu.krzdabrowski.currencyadder.basefeature.data.repository.ExchangeRatesRepositoryImpl
import eu.krzdabrowski.currencyadder.basefeature.domain.repository.ExchangeRatesRepository
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesThatStartWithUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ExchangeRatesModule {

    @Provides
    @Singleton
    fun provideExchangeRatesApi(retrofit: Retrofit): ExchangeRatesApi = retrofit.create(ExchangeRatesApi::class.java)

    @Provides
    fun provideGetAllCurrencyCodesUseCase(exchangeRatesRepository: ExchangeRatesRepository) =
        GetAllCurrencyCodesUseCase(exchangeRatesRepository::getAllCurrencyCodes)

    @Provides
    fun provideGetCurrencyCodesThatStartWithUseCase(exchangeRatesRepository: ExchangeRatesRepository) =
        GetCurrencyCodesThatStartWithUseCase(exchangeRatesRepository::getCurrencyCodesThatStartWith)

    @Provides
    fun provideRefreshExchangeRatesUseCase(exchangeRatesRepository: ExchangeRatesRepository) =
        RefreshExchangeRatesUseCase(exchangeRatesRepository::refreshExchangeRates)

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindExchangeRatesRepository(impl: ExchangeRatesRepositoryImpl): ExchangeRatesRepository
    }
}
