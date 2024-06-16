package eu.krzdabrowski.currencyadder.basefeature.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import eu.krzdabrowski.currencyadder.basefeature.data.local.model.UserSavingCached
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSavingsDao {

    // region [C]reate operations
    @Insert
    suspend fun addUserSaving(userSaving: UserSavingCached)
    // endregion

    // region [R]ead operations
    @Query("SELECT * FROM User_Savings")
    fun getUserSavings(): Flow<List<UserSavingCached>>

    @Query(
        "SELECT SUM(savings.amount * rates.value) " +
            "FROM User_Savings savings " +
            "JOIN Exchange_Rates rates ON rates.code = savings.currency",
    )
    fun getTotalUserSavingsInBaseCurrency(): Flow<Double>
    // endregion

    // region [U]pdate operations
    @Update
    suspend fun updateUserSaving(userSaving: UserSavingCached)

    @Transaction
    suspend fun swapUserSavings(
        fromIndex: Long,
        toIndex: Long,
    ) {
        // to prevent violation of an unique id constraint

        setSwappedUserSavingsIdsNegative(fromIndex, toIndex)
        setSwappedUserSavingsIdsPositive()
    }

    @Query(
        "UPDATE User_Savings " +
            "SET id = (CASE WHEN id = :fromIndex THEN -:toIndex else -:fromIndex END) " +
            "WHERE id in (:fromIndex, :toIndex)",
    )
    suspend fun setSwappedUserSavingsIdsNegative(
        fromIndex: Long,
        toIndex: Long,
    )

    @Query(
        "UPDATE User_Savings " +
            "SET id = -id " +
            "WHERE id < 0",
    )
    suspend fun setSwappedUserSavingsIdsPositive()
    // endregion

    // region [D]elete operations
    @Transaction
    suspend fun removeUserSaving(userSavingId: Long) {
        deleteUserSaving(userSavingId)
        updateUserSavingsIdsGreaterThan(userSavingId)
    }

    @Query(
        "DELETE FROM User_Savings " +
            "WHERE id = :userSavingId",
    )
    suspend fun deleteUserSaving(userSavingId: Long)

    @Query(
        "UPDATE User_Savings " +
            "SET id = id - 1 " +
            "WHERE id > :userSavingId",
    )
    suspend fun updateUserSavingsIdsGreaterThan(userSavingId: Long)
    // endregion
}
