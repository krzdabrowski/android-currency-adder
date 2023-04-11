package eu.krzdabrowski.currencyadder.basefeature.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.UserSavingCached
import eu.krzdabrowski.currencyadder.basefeature.data.repository.BASE_EXCHANGE_RATE_CODE
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSavingsDao {

    @Query("SELECT * FROM User_Savings")
    fun getUserSavings(): Flow<List<UserSavingCached>>

    @Query(
        "SELECT ROUND(total_savings.amount / chosen_currency.rate, 2) FROM " +
            "(" +
            "SELECT SUM(savings.amount * rates.value) AS amount " +
            "FROM User_Savings savings " +
            "JOIN Exchange_Rates rates ON rates.code = savings.currency" +
            ") AS total_savings, " +
            "(" +
            "SELECT rates.value AS rate " +
            "FROM Exchange_Rates rates " +
            "WHERE code = :currencyCode " +
            "LIMIT 1" +
            ") AS chosen_currency"
    )
    fun getTotalUserSavingsInChosenCurrency(currencyCode: String = BASE_EXCHANGE_RATE_CODE): Flow<Double>

    @Insert
    suspend fun addUserSaving(userSaving: UserSavingCached)

    @Update
    suspend fun updateUserSaving(userSaving: UserSavingCached)

    @Delete
    suspend fun removeUserSaving(userSaving: UserSavingCached)
}
