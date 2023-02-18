package eu.krzdabrowski.currencyadder.basefeature.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.krzdabrowski.currencyadder.basefeature.presentation.RocketsNavigationFactory
import eu.krzdabrowski.currencyadder.basefeature.presentation.RocketsUiState
import eu.krzdabrowski.currencyadder.core.navigation.NavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RocketsViewModelModule {

    @Provides
    fun provideInitialRocketsUiState(): RocketsUiState = RocketsUiState()
}

@Module
@InstallIn(SingletonComponent::class)
interface RocketsSingletonModule {

    @Singleton
    @Binds
    @IntoSet
    fun bindRocketsNavigationFactory(factory: RocketsNavigationFactory): NavigationFactory
}
