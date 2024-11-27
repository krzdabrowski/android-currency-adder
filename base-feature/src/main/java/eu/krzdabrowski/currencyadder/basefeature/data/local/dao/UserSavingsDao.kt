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
    @Query("SELECT * FROM User_Savings ORDER BY positionIndex")
    fun getUserSavings(): Flow<List<UserSavingCached>>

    @Query("""
        SELECT SUM(savings.amount * rates.value)
        FROM User_Savings savings
        JOIN Exchange_Rates rates ON rates.code = savings.currency
    """)
    fun getTotalUserSavingsInBaseCurrency(): Flow<Double>
    // endregion

    // region [U]pdate operations
    @Update
    suspend fun updateUserSaving(userSaving: UserSavingCached)

    @Transaction
    suspend fun updateUserSavingPositions(
        movedItemId: Long,
        fromPosition: Int,
        toPosition: Int,
    ) {
        if (fromPosition > toPosition) {
            incrementPositions(fromPosition, toPosition)
        } else if (fromPosition < toPosition) {
            decrementPositions(fromPosition, toPosition)
        }

        updateItemPosition(movedItemId, toPosition)
    }

    @Query("""
        UPDATE User_Savings
        SET positionIndex = positionIndex + 1
        WHERE positionIndex >= :toPosition AND positionIndex < :fromPosition
    """)
    suspend fun incrementPositions(fromPosition: Int, toPosition: Int)

    @Query("""
        UPDATE User_Savings
        SET positionIndex = positionIndex - 1
        WHERE positionIndex > :fromPosition AND positionIndex <= :toPosition
    """)
    suspend fun decrementPositions(fromPosition: Int, toPosition: Int)

    @Query("UPDATE User_Savings SET positionIndex = :toPosition WHERE id = :itemId")
    suspend fun updateItemPosition(itemId: Long, toPosition: Int)
    // endregion

    // region [D]elete operations
    @Transaction
    suspend fun removeUserSaving(userSavingId: Long) {
        val position = getPositionIndexById(userSavingId)
        deleteUserSaving(userSavingId)
        updateUserSavingsPositionsGreaterThan(position)
    }

    @Query("SELECT positionIndex FROM User_Savings WHERE id = :userSavingId")
    suspend fun getPositionIndexById(userSavingId: Long): Int

    @Query("""
        DELETE FROM User_Savings
        WHERE id = :userSavingId
    """)
    suspend fun deleteUserSaving(userSavingId: Long)

    @Query("""
        UPDATE User_Savings
        SET positionIndex = positionIndex - 1
        WHERE positionIndex > :position
    """)
    suspend fun updateUserSavingsPositionsGreaterThan(position: Int)
    // endregion
}
