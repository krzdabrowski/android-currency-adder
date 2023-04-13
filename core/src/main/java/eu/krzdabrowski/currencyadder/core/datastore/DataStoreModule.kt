package eu.krzdabrowski.currencyadder.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore("settings")

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Singleton
    @Provides
    fun @receiver:ApplicationContext Context.provideDataStore(): DataStore<Preferences> =
        settingsDataStore
}
