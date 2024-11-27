package eu.krzdabrowski.currencyadder.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import javax.inject.Singleton

private const val APP_DATABASE_NAME = "app_database_name"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME,
    )
        .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
        // it is quite impossible to migrate data between v1.0 and v1.1 app versions
        .fallbackToDestructiveMigrationFrom(1)
        .build()

    @Singleton
    @Provides
    fun provideExchangeRatesDao(database: AppDatabase): ExchangeRatesDao = database.exchangeRatesDao()

    @Singleton
    @Provides
    fun provideUserSavingsDao(database: AppDatabase): UserSavingsDao = database.userSavingsDao()
}
