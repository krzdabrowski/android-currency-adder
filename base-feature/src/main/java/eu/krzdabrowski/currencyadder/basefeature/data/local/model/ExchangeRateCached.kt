package eu.krzdabrowski.currencyadder.basefeature.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exchange_Rates")
data class ExchangeRateCached(

    @PrimaryKey
    @ColumnInfo(name = "code")
    val currencyCode: String,

    @ColumnInfo(name = "value")
    val currencyRate: Double,
)
