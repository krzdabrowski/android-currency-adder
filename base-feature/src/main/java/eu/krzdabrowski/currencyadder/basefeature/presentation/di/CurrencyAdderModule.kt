package eu.krzdabrowski.currencyadder.basefeature.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderNavigationFactory
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState
import eu.krzdabrowski.currencyadder.core.navigation.NavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object CurrencyAdderViewModelModule {

    @Provides
    fun provideInitialCurrencyAdderUiState(): CurrencyAdderUiState = CurrencyAdderUiState()
}

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyAdderSingletonModule {

    @Singleton
    @Binds
    @IntoSet
    fun bindCurrencyAdderNavigationFactory(factory: CurrencyAdderNavigationFactory): NavigationFactory
}
