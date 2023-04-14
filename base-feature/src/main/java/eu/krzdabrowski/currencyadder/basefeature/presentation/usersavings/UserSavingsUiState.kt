package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class UserSavingsUiState(
    val isLoading: Boolean = false,
    val userSavings: List<UserSavingDisplayable> = emptyList(),
    val currencyCodes: List<String> = emptyList(),
    val isError: Boolean = false,
) : Parcelable {

    sealed interface PartialState {
        sealed interface UserSavingsPartialState : PartialState {
            object Loading : UserSavingsPartialState // for simplicity: initial loading & refreshing
            data class UserSavingsFetched(val userSavings: List<UserSavingDisplayable>) : UserSavingsPartialState

            data class Error(val throwable: Throwable) : UserSavingsPartialState
        }
        data class CurrencyCodesFetched(val currencyCodes: List<String>) : PartialState
    }
}
