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
    suspend fun updateUserSavingPositions(
        movedItemId: Long,
        fromIndex: Long,
        toIndex: Long,
    ) {
        if (fromIndex < toIndex) {
            decrementPositions(fromIndex, toIndex)
        } else if (fromIndex > toIndex) {
            incrementPositions(fromIndex, toIndex)
        }
        updateItemPosition(movedItemId, toIndex)
    }

    @Query("""
        UPDATE User_Savings
        SET id = id - 1
        WHERE id > :fromPosition AND id <= :toPosition
    """)
    suspend fun decrementPositions(fromPosition: Long, toPosition: Long)

    @Query("""
        UPDATE User_Savings
        SET id = id + 1
        WHERE id >= :toPosition AND id < :fromPosition
    """)
    suspend fun incrementPositions(fromPosition: Long, toPosition: Long)

    @Query("UPDATE User_Savings SET id = :toPosition WHERE id = :itemId")
    suspend fun updateItemPosition(itemId: Long, toPosition: Long)

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
