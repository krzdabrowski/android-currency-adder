package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.ChooseUserSavingCurrency
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderViewModel
import eu.krzdabrowski.currencyadder.core.extensions.collectAsStateWithLifecycle
import eu.krzdabrowski.currencyadder.core.extensions.collectWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun CurrencyAdderRoute(
    viewModel: CurrencyAdderViewModel = hiltViewModel()
) {
    HandleEvents(viewModel.event)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CurrencyAdderScreen(
        uiState = uiState,
        onRefreshExchangeRates = {
            viewModel.acceptIntent(RefreshExchangeRates)
        },
        onUserSavingCurrencyClicked = {
            viewModel.acceptIntent(ChooseUserSavingCurrency(it))
        },
        onAddUserSaving = {
            viewModel.acceptIntent(AddUserSaving)
        }
    )
}

@Composable
internal fun CurrencyAdderScreen(
    uiState: CurrencyAdderUiState,
    onRefreshExchangeRates: () -> Unit,
    onUserSavingCurrencyClicked: (Int) -> Unit,
    onAddUserSaving: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddUserSaving() }) {
                    Icon(Icons.Filled.Add, stringResource(R.string.add_user_saving_content_description))
            }
        }
    ) {
        // TODO: migrate from accompanist to built-in pull-to-refresh when added to Material3
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = onRefreshExchangeRates,
            modifier = Modifier
                .padding(it)
        ) {
            if (uiState.userSavings.isNotEmpty()) {
                CurrencyAdderAvailableContent(
                    snackbarHostState = snackbarHostState,
                    uiState = uiState,
                    onUserSavingClick = onUserSavingCurrencyClicked
                )
            } else {
                CurrencyAdderNotAvailableContent()
            }
        }
    }
}

@Composable
private fun HandleEvents(events: Flow<CurrencyAdderEvent>) {
    events.collectWithLifecycle { }
}

@Composable
private fun CurrencyAdderAvailableContent(
    snackbarHostState: SnackbarHostState,
    uiState: CurrencyAdderUiState,
    onUserSavingClick: (Int) -> Unit
) {
    if (uiState.isError) {
        val errorMessage = stringResource(R.string.exchange_rates_error_refreshing)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }

    CurrencyAdderListContent(
        userSavingList = uiState.userSavings,
        onUserSavingClick = { onUserSavingClick(it) }
    )
}

@Composable
private fun CurrencyAdderNotAvailableContent() {
    Text(
        text = "Add some savings!"
    )
}
