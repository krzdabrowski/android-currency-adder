package eu.krzdabrowski.currencyadder.basefeature.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Savings")
data class UserSavingCached(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "positionIndex")
    val position: Int,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "currency")
    val currency: String,
)
