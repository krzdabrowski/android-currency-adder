package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UpdateChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderViewModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.extensions.collectWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun CurrencyAdderRoute(
    viewModel: CurrencyAdderViewModel = hiltViewModel(),
) {
    HandleEvents(viewModel.event)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CurrencyAdderScreen(
        uiState = uiState,
        onRefreshExchangeRates = {
            viewModel.acceptIntent(RefreshExchangeRates)
        },
        onAddUserSaving = {
            viewModel.acceptIntent(AddUserSaving)
        },
        onUpdateUserSaving = {
            viewModel.acceptIntent(UpdateUserSaving(it))
        },
        onRemoveUserSaving = {
            viewModel.acceptIntent(RemoveUserSaving(it))
        },
        onUpdateChosenCurrencyCodeForTotalSavings = {
            viewModel.acceptIntent(UpdateChosenCurrencyCodeForTotalSavings(it))
        }
    )
}

@Composable
internal fun CurrencyAdderScreen(
    uiState: CurrencyAdderUiState,
    onRefreshExchangeRates: () -> Unit,
    onAddUserSaving: () -> Unit,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (UserSavingDisplayable) -> Unit,
    onUpdateChosenCurrencyCodeForTotalSavings: (String) -> Unit
) {
    Column {
        CurrencyAdderScreenContent(
            uiState = uiState,
            modifier = Modifier.fillMaxHeight(0.85f),
            onRefreshExchangeRates = onRefreshExchangeRates,
            onAddUserSaving = onAddUserSaving,
            onUpdateUserSaving = onUpdateUserSaving,
            onRemoveUserSaving = onRemoveUserSaving
        )

        CurrencyAdderTotalSavingsContent(
            uiState = uiState,
            onGetTotalUserSavingsInChosenCurrency = onUpdateChosenCurrencyCodeForTotalSavings,
            modifier = Modifier.wrapContentHeight()
        )
    }
}

@Composable
private fun HandleEvents(events: Flow<CurrencyAdderEvent>) {
    events.collectWithLifecycle { }
}
