package eu.krzdabrowski.currencyadder.basefeature.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class CurrencyAdderUiState(
    val isLoading: Boolean = false,
    val userSavings: List<UserSavingDisplayable> = emptyList(),
    val currencyCodes: List<String> = emptyList(),
    val isError: Boolean = false,
) : Parcelable {

    sealed class PartialState {
        object Loading : PartialState() // for simplicity: initial loading & refreshing

        data class UserSavingsFetched(val userSavings: List<UserSavingDisplayable>) : PartialState()

        data class CurrencyCodesFetched(val currencyCodes: List<String>) : PartialState()

        data class Error(val throwable: Throwable) : PartialState()
    }
}
