package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.GetUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UserSavingAmountChanged
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UserSavingCurrencyClicked
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UserSavingLocationChanged
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
    private val getUserSavingsUseCase: GetUserSavingsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    savedStateHandle: SavedStateHandle,
    currencyAdderInitialState: CurrencyAdderUiState
) : BaseViewModel<CurrencyAdderUiState, PartialState, CurrencyAdderEvent, CurrencyAdderIntent>(
    savedStateHandle,
    currencyAdderInitialState
) {
    init {
        acceptIntent(RefreshExchangeRates)
        acceptIntent(GetUserSavings)
    }

    override fun mapIntents(intent: CurrencyAdderIntent): Flow<PartialState> = when (intent) {
        is GetUserSavings -> getUserSavings()
        is RefreshExchangeRates -> refreshExchangeRates()
        is UserSavingCurrencyClicked -> userSavingCurrencyClicked(intent.userSavingId)
        is UserSavingAmountChanged -> emptyFlow()
        is UserSavingLocationChanged -> emptyFlow()
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
            userSavings = partialState.list,
            isError = false
        )
        is Error -> previousState.copy(
            isLoading = false,
            isError = true
        )
    }

    private fun getUserSavings(): Flow<PartialState> = flow {
        getUserSavingsUseCase()
            .onStart {
                emit(Loading)
            }
            .collect { result ->
                result
                    .onSuccess { userSavingList ->
                        emit(Fetched(userSavingList.map { it.toPresentationModel() }))
                    }
                    .onFailure {
                        emit(Error(it))
                    }
            }
    }

    private fun refreshExchangeRates(): Flow<PartialState> = flow {
        refreshExchangeRatesUseCase()
            .onFailure {
                emit(Error(it))
            }
    }

    private fun userSavingCurrencyClicked(userSavingId: Int): Flow<PartialState> {
        return emptyFlow()
    }
}
