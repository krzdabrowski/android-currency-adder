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
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE_NAME,
        )
            // it is quite impossible to migrate data between v1.0 and v1.1 app versions
            // and make app not bugged due to lack of information when user saving was added in v1.0 (timestamp)
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }

    @Singleton
    @Provides
    fun provideExchangeRatesDao(database: AppDatabase): ExchangeRatesDao {
        return database.exchangeRatesDao()
    }

    @Singleton
    @Provides
    fun provideUserSavingsDao(database: AppDatabase): UserSavingsDao {
        return database.userSavingsDao()
    }
}
