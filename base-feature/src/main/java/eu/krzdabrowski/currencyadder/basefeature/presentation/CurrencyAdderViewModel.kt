package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderEvent.OpenWebBrowserWithDetails
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.GetRockets
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshRockets
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RocketClicked
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Fetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.core.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val HTTP_PREFIX = "http"
private const val HTTPS_PREFIX = "https"

@HiltViewModel
class CurrencyAdderViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    savedStateHandle: SavedStateHandle,
    currencyAdderInitialState: CurrencyAdderUiState
) : BaseViewModel<CurrencyAdderUiState, PartialState, CurrencyAdderEvent, CurrencyAdderIntent>(
    savedStateHandle,
    currencyAdderInitialState
) {
    init {
        acceptIntent(GetRockets)
    }

    override fun mapIntents(intent: CurrencyAdderIntent): Flow<PartialState> = when (intent) {
        is GetRockets -> getRockets()
        is RefreshRockets -> refreshRockets()
        is RocketClicked -> rocketClicked(intent.uri)
    }

    override fun reduceUiState(
        previousState: CurrencyAdderUiState,
        partialState: PartialState
    ): CurrencyAdderUiState = when (partialState) {
        is Loading -> previousState.copy(
            isLoading = true,
            isError = false
        )
        is Fetched -> previousState.copy(
            isLoading = false,
            rockets = partialState.list,
            isError = false
        )
        is Error -> previousState.copy(
            isLoading = false,
            isError = true
        )
    }

    private fun getRockets(): Flow<PartialState> = flow {
        getExchangeRatesUseCase()
            .onStart {
                emit(Loading)
            }
            .collect { result ->
                result
                    .onSuccess { rocketList ->
                        emit(Fetched(rocketList.map { it.toPresentationModel() }))
                    }
                    .onFailure {
                        emit(Error(it))
                    }
            }
    }

    private fun refreshRockets(): Flow<PartialState> = flow {
        refreshExchangeRatesUseCase()
            .onFailure {
                emit(Error(it))
            }
    }

    private fun rocketClicked(uri: String): Flow<PartialState> {
        if (uri.startsWith(HTTP_PREFIX) || uri.startsWith(HTTPS_PREFIX)) {
            publishEvent(OpenWebBrowserWithDetails(uri))
        }

        return emptyFlow()
    }
}
