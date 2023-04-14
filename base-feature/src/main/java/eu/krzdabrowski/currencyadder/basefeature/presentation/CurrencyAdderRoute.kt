package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.UpdateChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsViewModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.composable.TotalSavingsContent
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsViewModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable.UserSavingsContent
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

@Composable
fun CurrencyAdderRoute(
    userSavingsViewModel: UserSavingsViewModel = hiltViewModel(),
    totalSavingsViewModel: TotalSavingsViewModel = hiltViewModel(),
) {
    val userSavingsUiState by userSavingsViewModel.uiState.collectAsStateWithLifecycle()
    val totalSavingsUiState by totalSavingsViewModel.uiState.collectAsStateWithLifecycle()

    CurrencyAdderScreen(
        userSavingsUiState = userSavingsUiState,
        totalSavingsUiState = totalSavingsUiState,
        onAddUserSaving = {
            userSavingsViewModel.acceptIntent(AddUserSaving)
        },
        onUpdateUserSaving = {
            userSavingsViewModel.acceptIntent(UpdateUserSaving(it))
        },
        onRemoveUserSaving = {
            userSavingsViewModel.acceptIntent(RemoveUserSaving(it))
        },
        onRefreshExchangeRates = {
            userSavingsViewModel.acceptIntent(RefreshExchangeRates)
        },
        onUpdateChosenCurrencyCodeForTotalSavings = {
            totalSavingsViewModel.acceptIntent(UpdateChosenCurrencyCodeForTotalSavings(it))
        }
    )
}

@Composable
private fun CurrencyAdderScreen(
    userSavingsUiState: UserSavingsUiState,
    totalSavingsUiState: TotalSavingsUiState,
    onAddUserSaving: () -> Unit,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (UserSavingDisplayable) -> Unit,
    onRefreshExchangeRates: () -> Unit,
    onUpdateChosenCurrencyCodeForTotalSavings: (String) -> Unit
) {
    Column {
        UserSavingsContent(
            uiState = userSavingsUiState,
            modifier = Modifier.fillMaxHeight(0.85f),
            onAddUserSaving = onAddUserSaving,
            onUpdateUserSaving = onUpdateUserSaving,
            onRemoveUserSaving = onRemoveUserSaving,
            onRefreshExchangeRates = onRefreshExchangeRates,
        )

        TotalSavingsContent(
            uiState = totalSavingsUiState,
            onGetTotalUserSavingsInChosenCurrency = onUpdateChosenCurrencyCodeForTotalSavings,
            modifier = Modifier.wrapContentHeight()
        )
    }
}
