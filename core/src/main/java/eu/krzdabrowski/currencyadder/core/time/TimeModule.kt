package eu.krzdabrowski.currencyadder.core.time

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TimeModule {

    @Provides
    @Singleton
    fun provideSystemClock(): Clock.System = Clock.System
}
