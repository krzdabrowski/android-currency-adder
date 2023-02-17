package eu.krzdabrowski.currencyadder.basicfeature.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exchange_Rates")
data class ExchangeRateCached(

    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "currency_rate")
    val currencyRate: Double
)
