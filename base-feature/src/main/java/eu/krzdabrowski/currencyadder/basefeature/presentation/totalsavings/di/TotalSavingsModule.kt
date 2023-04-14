package eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState

@Module
@InstallIn(ViewModelComponent::class)
internal object TotalSavingsModule {

    @Provides
    fun provideInitialTotalSavingsUiState(): TotalSavingsUiState = TotalSavingsUiState()
}
