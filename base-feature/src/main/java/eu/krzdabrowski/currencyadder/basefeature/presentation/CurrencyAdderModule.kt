package eu.krzdabrowski.currencyadder.basefeature.presentation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import eu.krzdabrowski.currencyadder.core.navigation.NavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CurrencyAdderModule {

    @Singleton
    @Binds
    @IntoSet
    fun bindCurrencyAdderNavigationFactory(factory: CurrencyAdderNavigationFactory): NavigationFactory
}
