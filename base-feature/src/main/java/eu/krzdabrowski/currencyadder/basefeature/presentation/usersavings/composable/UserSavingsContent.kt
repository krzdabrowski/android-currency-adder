package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable

private val headerStringResources = listOf(
    R.string.list_header_place,
    R.string.list_header_saving,
    R.string.list_header_currency,
)

@Composable
fun UserSavingsContent(
    uiState: UserSavingsUiState,
    onAddUserSaving: () -> Unit,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (UserSavingDisplayable) -> Unit,
    onRefreshExchangeRates: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddUserSaving,
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_user_saving_content_description))
            }
        },
    ) {
        // TODO: migrate from accompanist to built-in pull-to-refresh when added to Material3
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = onRefreshExchangeRates,
            modifier = Modifier
                .padding(it),
        ) {
            if (uiState.userSavings.isNotEmpty()) {
                UserSavingsAvailableContent(
                    snackbarHostState = snackbarHostState,
                    uiState = uiState,
                    onUpdateUserSaving = onUpdateUserSaving,
                    onRemoveUserSaving = onRemoveUserSaving,
                )
            } else {
                UserSavingsNotAvailableContent()
            }
        }
    }
}

@Composable
private fun UserSavingsAvailableContent(
    snackbarHostState: SnackbarHostState,
    uiState: UserSavingsUiState,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (UserSavingDisplayable) -> Unit,
) {
    if (uiState.isError) {
        val errorMessage = stringResource(R.string.exchange_rates_error_refreshing)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
            )
        }
    }

    UserSavingsListContent(
        uiState = uiState,
        onUpdateUserSaving = onUpdateUserSaving,
        onRemoveUserSaving = onRemoveUserSaving,
    )
}

@Composable
private fun UserSavingsNotAvailableContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.list_empty_message),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@VisibleForTesting
@Composable
internal fun UserSavingsListContent(
    uiState: UserSavingsUiState,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (UserSavingDisplayable) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        header()

        items(
            items = uiState.userSavings,
            key = { userSaving -> userSaving.id },
        ) {
            UserSavingItem(
                item = it,
                currencyCodes = uiState.currencyCodes,
                modifier = Modifier.animateItemPlacement(),
                onItemUpdate = onUpdateUserSaving,
                onItemRemove = onRemoveUserSaving,
            )

            Divider(color = Color.Black)
        }
    }
}

private fun LazyListScope.header() {
    stickyHeader {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    vertical = dimensionResource(R.dimen.dimen_small),
                ),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            for (headerId in headerStringResources) {
                Text(
                    text = stringResource(headerId),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
