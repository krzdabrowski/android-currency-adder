package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import eu.krzdabrowski.currencyadder.core.utils.DraggableItem
import eu.krzdabrowski.currencyadder.core.utils.dragContainer
import eu.krzdabrowski.currencyadder.core.utils.rememberDragDropState

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
    onRemoveUserSaving: (Long) -> Unit,
    onDragAndDropUserSaving: (Int, Int) -> Unit,
    getCurrencyCodesThatStartWith: (String, Long) -> Unit,
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
                    onDragAndDropUserSaving = onDragAndDropUserSaving,
                    getCurrencyCodesThatStartWith = getCurrencyCodesThatStartWith,
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
    onRemoveUserSaving: (Long) -> Unit,
    onDragAndDropUserSaving: (Int, Int) -> Unit,
    getCurrencyCodesThatStartWith: (String, Long) -> Unit,
) {
    if (uiState.isError) {
        val errorMessage = stringResource(R.string.exchange_rates_error_refreshing)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
            )
        }
    }

    Column {
        UserSavingsHeader()

        UserSavingsListContent(
            uiState = uiState,
            onUpdateUserSaving = onUpdateUserSaving,
            onRemoveUserSaving = onRemoveUserSaving,
            onDragAndDropUserSaving = onDragAndDropUserSaving,
            getCurrencyCodesThatStartWith = getCurrencyCodesThatStartWith,
        )
    }
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

@Composable
private fun UserSavingsHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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

@VisibleForTesting
@Composable
internal fun UserSavingsListContent(
    uiState: UserSavingsUiState,
    onUpdateUserSaving: (UserSavingDisplayable) -> Unit,
    onRemoveUserSaving: (Long) -> Unit,
    onDragAndDropUserSaving: (Int, Int) -> Unit,
    getCurrencyCodesThatStartWith: (String, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        lazyListState = listState,
        onMove = onDragAndDropUserSaving,
    )

    LazyColumn(
        modifier = modifier
            .dragContainer(dragDropState),
        state = listState,
    ) {
        itemsIndexed(
            items = uiState.userSavings,
            key = { _, userSaving -> (userSaving.id ?: 0L) to userSaving.timestamp },
        ) { index, item ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index,
            ) { isDragging ->
                UserSavingItem(
                    item = item,
                    onItemUpdate = onUpdateUserSaving,
                    onItemRemove = onRemoveUserSaving,
                    onCurrencyCodesUpdate = {
                        getCurrencyCodesThatStartWith(it, item.id ?: 0L)
                    },
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = !isDragging,
                        ) { /* no-op */ }
                        .background(
                            if (isDragging) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                        ),
                )
            }

            Divider(color = Color.Black)
        }
    }
}
