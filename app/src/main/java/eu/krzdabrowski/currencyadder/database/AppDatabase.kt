package eu.krzdabrowski.currencyadder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.dao.UserSavingsDao
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.ExchangeRateCached
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.UserSavingCached

private const val DATABASE_VERSION = 3

@Database(
    version = DATABASE_VERSION,
    entities = [ExchangeRateCached::class, UserSavingCached::class],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exchangeRatesDao(): ExchangeRatesDao

    abstract fun userSavingsDao(): UserSavingsDao
}
