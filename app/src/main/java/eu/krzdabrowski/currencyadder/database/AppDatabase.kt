package eu.krzdabrowski.currencyadder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.krzdabrowski.currencyadder.basicfeature.data.local.dao.ExchangeRatesDao
import eu.krzdabrowski.currencyadder.basicfeature.data.local.model.ExchangeRateCached

private const val DATABASE_VERSION = 1

@Database(
    entities = [ExchangeRateCached::class],
    version = DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRatesDao(): ExchangeRatesDao
}
