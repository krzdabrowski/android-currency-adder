package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent.OpenWebBrowserWithDetails
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshRockets
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RocketClicked
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
        onRefreshRockets = {
            viewModel.acceptIntent(RefreshRockets)
        },
        onRocketClicked = {
            viewModel.acceptIntent(RocketClicked(it))
        }
    )
}

@Composable
internal fun CurrencyAdderScreen(
    uiState: CurrencyAdderUiState,
    onRefreshRockets: () -> Unit,
    onRocketClicked: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        // TODO: migrate from accompanist to built-in pull-to-refresh when added to Material3
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = onRefreshRockets,
            modifier = Modifier
                .padding(it)
        ) {
            if (uiState.rockets.isNotEmpty()) {
                CurrencyAdderAvailableContent(
                    snackbarHostState = snackbarHostState,
                    uiState = uiState,
                    onRocketClick = onRocketClicked
                )
            } else {
                CurrencyAdderNotAvailableContent(
                    uiState = uiState
                )
            }
        }
    }
}

@Composable
private fun HandleEvents(events: Flow<CurrencyAdderEvent>) {
    val uriHandler = LocalUriHandler.current

    events.collectWithLifecycle {
        when (it) {
            is OpenWebBrowserWithDetails -> {
                uriHandler.openUri(it.uri)
            }
        }
    }
}

@Composable
private fun CurrencyAdderAvailableContent(
    snackbarHostState: SnackbarHostState,
    uiState: CurrencyAdderUiState,
    onRocketClick: (String) -> Unit
) {
    if (uiState.isError) {
        val errorMessage = stringResource(R.string.rockets_error_refreshing)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }

    CurrencyAdderListContent(
        exchangeRateList = uiState.rockets,
        onRocketClick = { onRocketClick(it) }
    )
}

@Composable
private fun CurrencyAdderNotAvailableContent(uiState: CurrencyAdderUiState) {
    when {
        uiState.isLoading -> CurrencyAdderLoadingPlaceholder()
        uiState.isError -> CurrencyAdderErrorContent()
    }
}
