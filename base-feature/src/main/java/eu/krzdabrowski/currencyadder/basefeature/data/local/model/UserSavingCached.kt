package eu.krzdabrowski.currencyadder.basefeature.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Savings")
data class UserSavingCached(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "saving")
    val saving: Double,

    @ColumnInfo(name = "currency")
    val currency: String
)
