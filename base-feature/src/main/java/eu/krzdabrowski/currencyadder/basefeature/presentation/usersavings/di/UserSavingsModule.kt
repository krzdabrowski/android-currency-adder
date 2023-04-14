package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState

@Module
@InstallIn(ViewModelComponent::class)
internal object UserSavingsModule {

    @Provides
    fun provideInitialUserSavingsUiState(): UserSavingsUiState = UserSavingsUiState()
}
