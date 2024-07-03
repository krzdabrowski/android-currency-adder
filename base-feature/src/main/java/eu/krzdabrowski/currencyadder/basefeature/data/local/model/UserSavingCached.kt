package eu.krzdabrowski.currencyadder.basefeature.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Savings")
data class UserSavingCached(

    // to not use AUTOINCREMENT as it is discouraged by SQLite
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "currency")
    val currency: String,
)
